# IDG-Prof: An Identity-Aware Multi-Source Modeling Framework for Graduate Development Profiling and Early Risk Intervention

[![Python](https://img.shields.io/badge/Python-3.9%2B-blue.svg)]()
[![License](https://img.shields.io/badge/License-MIT-green.svg)]()

## Overview

**IDG-Prof** is an identity-aware multi-source modeling framework for graduate development profiling and early risk intervention.  
The project integrates heterogeneous educational data, including academic records, behavioral information, and psychological-related signals, into a unified profiling framework, and further supports:

- adaptive academic assessment
- cluster-aware risk discovery
- identity-aware large language model (LLM) intervention
- personalized educational support

This repository contains the implementation of the IDG-Prof framework and the prototype system used for profiling, warning, and intervention.

---

## Key Features

- **Multi-source profile representation**  
  Jointly models heterogeneous academic, behavioral, and psychological-related data.

- **Adaptive academic assessment**  
  Supports seven-dimensional evaluation with quantitative scoring, relative evaluation, and nonlinear weighting.

- **Cluster-aware trajectory modeling**  
  Captures developmental heterogeneity and temporal evolution for early risk discovery.

- **Identity-aware LLM intervention**  
  Generates personalized responses conditioned on user role, profile state, and dialogue context.

- **Prototype system support**  
  Provides an end-to-end workflow from profile construction and warning generation to interactive intervention.

---

## Framework

The overall framework of IDG-Prof consists of four main modules:

1. **Multi-source Educational Data Representation**  
   Encodes heterogeneous student data into a unified graduate profile representation.

2. **Adaptive Academic Assessment**  
   Performs seven-dimensional academic evaluation through scoring, normalization, and nonlinear aggregation.

3. **Cluster-aware Trajectory Modeling for Risk Discovery**  
   Models longitudinal development trajectories and identifies potential risks.

4. **Identity-aware LLM Intervention**  
   Provides personalized support through profile-conditioned prompting and agent routing.

---

