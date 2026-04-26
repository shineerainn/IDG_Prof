import json
from typing import Any, Dict, List, Optional, Tuple

import numpy as np
import pandas as pd
from fastapi import APIRouter, Body
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
from sklearn.metrics import calinski_harabasz_score, silhouette_score
from sklearn.preprocessing import StandardScaler


idg_prof = APIRouter()


DIMENSIONS = {
    "course_performance": {
        "label": "course_performance",
        "score_field": "academicPerformanceScore",
        "fields": [
            "weightedAvgGrade",
            "coreCourseGrade",
            "lastSemesterAvgGrade",
            "gpaGrowthRate",
            "gradeRanking",
            "rankingLevel",
        ],
        "weight": 0.22,
    },
    "project_participation": {
        "label": "project_participation",
        "score_field": "researchProjectScore",
        "fields": [
            "researchProjectLevel",
            "projectRole",
            "projectAchievementTransformation",
        ],
        "weight": 0.16,
    },
    "thesis_progress": {
        "label": "thesis_progress",
        "score_field": "thesisScore",
        "fields": [
            "thesisDefenseGrade",
            "thesisInnovativeness",
            "thesisPracticalValue",
            "thesisAchievementTransformation",
        ],
        "weight": 0.16,
    },
    "academic_publications": {
        "label": "academic_publications",
        "score_field": "academicPaperScore",
        "fields": [
            "publishedJournalConference",
            "authorLevel",
            "isHighlyCited",
            "isIfAbove",
            "isCoverPaper",
        ],
        "weight": 0.16,
    },
    "competition_awards": {
        "label": "competition_awards",
        "score_field": "competitionScore",
        "fields": [
            "competitionLevel",
            "awardLevel",
            "competitionScope",
            "competitionTeamRole",
        ],
        "weight": 0.11,
    },
    "patent_applications": {
        "label": "patent_applications",
        "score_field": "patentSoftwareScore",
        "fields": [
            "patentSoftwareType",
            "legalStatusCoefficient",
            "technologyTransferAmount",
        ],
        "weight": 0.09,
    },
    "innovation_achievements": {
        "label": "innovation_achievements",
        "score_field": "otherInnovationAchievementScore",
        "fields": [
            "hasStartupProject",
            "startupFundingStage",
            "isCompanyRegistered",
            "hasStartupCompetitionAward",
            "techApplicationEnterpriseCount",
            "techPromotionContractAmount",
        ],
        "weight": 0.10,
    },
}

SOURCE_GROUPS = {
    "academic": [
        "academicPerformanceScore",
        "weightedAvgGrade",
        "coreCourseGrade",
        "lastSemesterAvgGrade",
        "gpaGrowthRate",
    ],
    "research": [
        "researchProjectScore",
        "thesisScore",
        "academicPaperScore",
        "competitionScore",
        "patentSoftwareScore",
        "otherInnovationAchievementScore",
    ],
    "behavior": [
        "libraryVisitCount",
        "dormAccessCount",
        "campusCardConsumption",
        "wifiOnlineHours",
        "forumPostCount",
    ],
    "psychological": [
        "psychologicalRiskScore",
        "psyRiskScore",
        "abnormalityScore",
        "counselorWarningCount",
    ],
}

DEFAULT_SOURCE_WEIGHTS = {
    "academic": 0.45,
    "research": 0.35,
    "behavior": 0.12,
    "psychological": 0.08,
}


def convert_numpy(obj):
    if isinstance(obj, (np.integer,)):
        return int(obj)
    if isinstance(obj, (np.floating,)):
        return float(obj)
    if isinstance(obj, np.ndarray):
        return obj.tolist()
    return obj


def _safe_float(value: Any, default: float = 0.0) -> float:
    if value is None or value == "":
        return default
    if isinstance(value, (int, float, np.integer, np.floating)):
        return float(value)
    if isinstance(value, str):
        normalized = value.strip().replace("%", "")
        if normalized.lower() in {"true", "yes", "y"}:
            return 1.0
        if normalized.lower() in {"false", "no", "n"}:
            return 0.0
        try:
            return float(normalized)
        except ValueError:
            return default
    return default


def _numeric_frame(records: List[Dict[str, Any]], fields: List[str]) -> pd.DataFrame:
    df = pd.DataFrame(records)
    for field in fields:
        if field not in df.columns:
            df[field] = 0.0
    numeric = df[fields].map(_safe_float).replace([np.inf, -np.inf], np.nan)
    return numeric.fillna(0.0)


def _normalize_weights(weights: Optional[Dict[str, float]], defaults: Dict[str, float]) -> Dict[str, float]:
    merged = defaults.copy()
    if weights:
        for key, value in weights.items():
            if key in merged:
                merged[key] = max(_safe_float(value), 0.0)
    total = sum(merged.values())
    if total <= 0:
        return defaults.copy()
    return {key: value / total for key, value in merged.items()}


def _percentile_rank(values: np.ndarray) -> np.ndarray:
    if len(values) <= 1:
        return np.ones_like(values, dtype=float) * 0.5
    order = pd.Series(values).rank(method="average", pct=True)
    return order.to_numpy(dtype=float)


class IDGProfAnalyzer:
    def __init__(
        self,
        dimension_weights: Optional[Dict[str, float]] = None,
        source_weights: Optional[Dict[str, float]] = None,
    ):
        self.dimension_weights = _normalize_weights(
            dimension_weights,
            {key: config["weight"] for key, config in DIMENSIONS.items()},
        )
        self.source_weights = _normalize_weights(source_weights, DEFAULT_SOURCE_WEIGHTS)
        self.scaler = StandardScaler()

    def build_dimension_scores(self, records: List[Dict[str, Any]]) -> Tuple[pd.DataFrame, pd.DataFrame]:
        raw_scores = pd.DataFrame(index=range(len(records)))
        detail_scores = pd.DataFrame(index=range(len(records)))

        for dimension, config in DIMENSIONS.items():
            score_field = config["score_field"]
            fields = [score_field] + config["fields"]
            frame = _numeric_frame(records, fields)
            explicit_score = frame[score_field]
            component_score = frame[config["fields"]].mean(axis=1)
            raw_scores[dimension] = np.where(explicit_score.abs() > 0, explicit_score, component_score)
            detail_scores[score_field] = raw_scores[dimension]

        return raw_scores.fillna(0.0), detail_scores.fillna(0.0)

    def discipline_normalize(self, records: List[Dict[str, Any]], raw_scores: pd.DataFrame) -> pd.DataFrame:
        meta = pd.DataFrame(records)
        context_key = None
        for candidate in ["discipline", "major", "college", "department", "grade"]:
            if candidate in meta.columns:
                context_key = candidate
                break

        normalized = pd.DataFrame(index=raw_scores.index)
        if context_key is None:
            for dimension in raw_scores.columns:
                std = raw_scores[dimension].std(ddof=0)
                normalized[dimension] = (raw_scores[dimension] - raw_scores[dimension].mean()) / (std + 1e-6)
            return normalized.fillna(0.0)

        groups = meta[context_key].fillna("global").astype(str)
        for dimension in raw_scores.columns:
            series = raw_scores[dimension]
            normalized[dimension] = series.groupby(groups).transform(
                lambda values: (values - values.mean()) / (values.std(ddof=0) + 1e-6)
            )
        return normalized.fillna(0.0)

    def nonlinear_enhance(self, normalized_scores: pd.DataFrame) -> pd.DataFrame:
        values = normalized_scores.to_numpy(dtype=float)
        enhanced = np.where(values >= 0, np.power(values + 1.0, 1.25) - 1.0, values)
        return pd.DataFrame(enhanced, columns=normalized_scores.columns, index=normalized_scores.index)

    def academic_assessment(self, records: List[Dict[str, Any]]) -> Dict[str, Any]:
        raw_scores, detail_scores = self.build_dimension_scores(records)
        normalized = self.discipline_normalize(records, raw_scores)
        enhanced = self.nonlinear_enhance(normalized)
        weights = np.array([self.dimension_weights[column] for column in enhanced.columns])
        relative_score = enhanced.to_numpy(dtype=float).dot(weights)
        percentile_score = _percentile_rank(relative_score) * 100.0

        return {
            "raw_scores": raw_scores,
            "detail_scores": detail_scores,
            "normalized_scores": normalized,
            "enhanced_scores": enhanced,
            "assessment_score": percentile_score,
            "dimension_weights": self.dimension_weights,
        }

    def unified_representation(self, records: List[Dict[str, Any]], normalized_dimensions: pd.DataFrame) -> pd.DataFrame:
        source_vectors = []
        source_names = []
        for source, fields in SOURCE_GROUPS.items():
            available = [field for field in fields if any(field in row for row in records)]
            if not available:
                encoded = pd.DataFrame({source: np.zeros(len(records))})
            else:
                frame = _numeric_frame(records, available)
                scaled = StandardScaler().fit_transform(frame) if len(frame) > 1 else frame.to_numpy(dtype=float)
                encoded = pd.DataFrame(scaled, columns=[f"{source}_{field}" for field in available])
            encoded = encoded * self.source_weights.get(source, 0.0)
            source_vectors.append(encoded.reset_index(drop=True))
            source_names.extend(encoded.columns.tolist())

        representation = pd.concat(
            [normalized_dimensions.reset_index(drop=True)] + source_vectors,
            axis=1,
        ).fillna(0.0)
        representation.columns = normalized_dimensions.columns.tolist() + source_names
        return representation

    def cluster_profiles(self, representation: pd.DataFrame, algorithm_config: Optional[Dict[str, Any]]) -> Dict[str, Any]:
        matrix = representation.to_numpy(dtype=float)
        if len(matrix) == 0:
            raise ValueError("empty profile data")
        if len(matrix) == 1:
            return {
                "labels": np.array([0]),
                "centroids": matrix,
                "pca": np.array([[0.0, 0.0]]),
                "metrics": {"silhouette_score": 0.0, "calinski_harabasz_score": 0.0},
            }

        params = (algorithm_config or {}).get("params", {})
        n_clusters = int(params.get("n_clusters", params.get("nClusters", 3)))
        n_clusters = max(2, min(n_clusters, len(matrix)))
        scaled = self.scaler.fit_transform(matrix)
        model = KMeans(
            n_clusters=n_clusters,
            init=params.get("init", "k-means++"),
            max_iter=int(params.get("max_iter", params.get("maxIter", 300))),
            n_init=int(params.get("n_init", params.get("nInit", 10))),
            random_state=int(params.get("random_state", params.get("randomState", 42))),
        )
        labels = model.fit_predict(scaled)
        pca_components = min(2, scaled.shape[1], scaled.shape[0])
        pca_data = PCA(n_components=pca_components).fit_transform(scaled)
        if pca_components == 1:
            pca_data = np.column_stack([pca_data[:, 0], np.zeros(len(pca_data))])

        metrics = {"silhouette_score": 0.0, "calinski_harabasz_score": 0.0}
        if len(set(labels)) > 1 and len(labels) > len(set(labels)):
            metrics["silhouette_score"] = float(silhouette_score(scaled, labels))
            metrics["calinski_harabasz_score"] = float(calinski_harabasz_score(scaled, labels))

        return {
            "labels": labels,
            "centroids": model.cluster_centers_,
            "pca": pca_data,
            "scaled": scaled,
            "metrics": metrics,
        }

    def risk_discovery(
        self,
        records: List[Dict[str, Any]],
        assessment_score: np.ndarray,
        clustering: Dict[str, Any],
    ) -> Dict[str, Any]:
        labels = clustering["labels"]
        scaled = clustering.get("scaled", np.zeros((len(labels), 1)))
        centroids = clustering["centroids"]
        distances = np.array([np.linalg.norm(scaled[i] - centroids[labels[i]]) for i in range(len(labels))])
        cluster_distance_risk = _percentile_rank(distances)

        low_score_risk = 1.0 - (assessment_score / 100.0)
        gpa_growth = np.array([_safe_float(row.get("gpaGrowthRate")) for row in records], dtype=float)
        last_score = np.array([_safe_float(row.get("lastSemesterAvgGrade")) for row in records], dtype=float)
        trend_drop = np.maximum(0.0, -gpa_growth / 20.0)
        if np.any(last_score):
            trend_drop += np.maximum(0.0, (np.nanmean(last_score) - last_score) / 100.0)
        trend_risk = np.clip(trend_drop, 0.0, 1.0)

        cluster_mean_score = pd.Series(assessment_score).groupby(labels).transform("mean").to_numpy(dtype=float)
        cluster_low_score_risk = 1.0 - (cluster_mean_score / 100.0)
        cluster_risk = np.clip(0.55 * cluster_distance_risk + 0.45 * cluster_low_score_risk, 0.0, 1.0)

        total_risk = np.clip(0.45 * low_score_risk + 0.25 * trend_risk + 0.30 * cluster_risk, 0.0, 1.0)
        levels = np.where(total_risk >= 0.70, "high", np.where(total_risk >= 0.45, "medium", "low"))

        return {
            "risk_score": total_risk,
            "risk_level": levels,
            "static_risk": low_score_risk,
            "trend_risk": trend_risk,
            "cluster_risk": cluster_risk,
        }

    def build(self, records: List[Dict[str, Any]], algorithm_config: Optional[Dict[str, Any]]) -> Dict[str, Any]:
        assessment = self.academic_assessment(records)
        representation = self.unified_representation(records, assessment["normalized_scores"])
        clustering = self.cluster_profiles(representation, algorithm_config)
        risk = self.risk_discovery(records, assessment["assessment_score"], clustering)

        enriched = []
        for idx, row in enumerate(records):
            item = dict(row)
            item["clusterResult"] = int(clustering["labels"][idx])
            item["academicAssessmentScore"] = round(float(assessment["assessment_score"][idx]), 4)
            item["riskScore"] = round(float(risk["risk_score"][idx]), 4)
            item["riskLevel"] = str(risk["risk_level"][idx])
            item["riskComponents"] = {
                "static": round(float(risk["static_risk"][idx]), 4),
                "trend": round(float(risk["trend_risk"][idx]), 4),
                "cluster": round(float(risk["cluster_risk"][idx]), 4),
            }
            item["profileVector"] = [round(float(v), 6) for v in representation.iloc[idx].to_numpy(dtype=float).tolist()]
            item["normalizedDimensionScores"] = {
                key: round(float(value), 4)
                for key, value in assessment["normalized_scores"].iloc[idx].to_dict().items()
            }
            item["enhancedDimensionScores"] = {
                key: round(float(value), 4)
                for key, value in assessment["enhanced_scores"].iloc[idx].to_dict().items()
            }
            enriched.append(item)

        cluster_profiles = {}
        for cluster_id in sorted(set(clustering["labels"].tolist())):
            mask = clustering["labels"] == cluster_id
            cluster_profiles[f"cluster_{int(cluster_id)}"] = {
                "cluster_id": int(cluster_id),
                "size": int(mask.sum()),
                "avg_assessment_score": round(float(np.mean(assessment["assessment_score"][mask])), 4),
                "avg_risk_score": round(float(np.mean(risk["risk_score"][mask])), 4),
                "risk_level_distribution": pd.Series(risk["risk_level"][mask]).value_counts().to_dict(),
            }

        return {
            "data": enriched,
            "clustering_result": {
                "cluster_labels": clustering["labels"].tolist(),
                "pca_data": clustering["pca"].tolist(),
                **clustering["metrics"],
            },
            "cluster_profiles": cluster_profiles,
            "profile_schema": representation.columns.tolist(),
            "dimension_weights": assessment["dimension_weights"],
            "source_weights": self.source_weights,
            "risk_summary": {
                "avg_risk_score": round(float(np.mean(risk["risk_score"])), 4) if len(records) else 0.0,
                "risk_level_distribution": pd.Series(risk["risk_level"]).value_counts().to_dict(),
            },
        }


@idg_prof.post("/academicProfile")
def build_academic_profile(
    academic_data: List[Dict[str, Any]] = Body(..., embed=True),
    algorithm_config: Optional[Dict[str, Any]] = Body(None, embed=True),
    dimension_weights: Optional[Dict[str, float]] = Body(None, embed=True),
    source_weights: Optional[Dict[str, float]] = Body(None, embed=True),
):
    try:
        analyzer = IDGProfAnalyzer(dimension_weights=dimension_weights, source_weights=source_weights)
        result = analyzer.build(academic_data, algorithm_config)
        return {
            "data": json.dumps(result["data"], default=convert_numpy, ensure_ascii=False),
            "clustering_result": result["clustering_result"],
            "cluster_profiles": result["cluster_profiles"],
            "profile_schema": result["profile_schema"],
            "dimension_weights": result["dimension_weights"],
            "source_weights": result["source_weights"],
            "risk_summary": result["risk_summary"],
            "success": True,
        }
    except Exception as exc:
        return {"error": str(exc), "data": None, "success": False}


@idg_prof.post("/agentRoute")
def route_agent(
    query: str = Body(..., embed=True),
    identity: str = Body("student", embed=True),
    profile_state: Optional[Dict[str, Any]] = Body(None, embed=True),
):
    text = (query or "").lower()
    identity = (identity or "student").lower()
    profile_state = profile_state or {}
    risk_level = str(profile_state.get("riskLevel", profile_state.get("risk_level", ""))).lower()

    scores = {
        "psychological_support": 0.0,
        "academic_diagnosis": 0.0,
        "decision_assistance": 0.0,
        "data_analysis": 0.0,
        "general_assistant": 0.0,
    }
    if any(keyword in text for keyword in ["心理", "焦虑", "压力", "情绪", "panic", "stress", "anxiety"]):
        scores["psychological_support"] += 2.0
    if any(keyword in text for keyword in ["成绩", "课程", "论文", "科研", "学业", "academic", "thesis"]):
        scores["academic_diagnosis"] += 2.0
    if any(keyword in text for keyword in ["建议", "规划", "选择", "导师", "毕业", "decision", "plan"]):
        scores["decision_assistance"] += 1.5
    if any(keyword in text for keyword in ["数据", "图表", "分析", "聚类", "画像", "chart", "cluster"]):
        scores["data_analysis"] += 1.5
    if risk_level == "high":
        scores["psychological_support"] += 0.8
        scores["academic_diagnosis"] += 0.5
    if identity in {"teacher", "supervisor", "counselor", "admin"}:
        scores["data_analysis"] += 0.4
        scores["decision_assistance"] += 0.4

    route = max(scores, key=scores.get)
    if scores[route] <= 0:
        route = "general_assistant"
    return {"agent": route, "scores": scores}
