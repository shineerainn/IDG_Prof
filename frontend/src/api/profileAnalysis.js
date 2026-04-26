import { get } from "@/utils/request";

export const fetchProfile = (role, params) =>
  get(`/profile/${role}/getRecord?profileId=${params}`);

export const fetchClusters = (role, params) =>
  get(`/profile/${role}/getClusterDistribution?profileId=${params}`);

export const fetchAllAttrs = (role, params) =>
  get(`/profile/${role}/getAllAttrMean?profileId=${params}`);

export const fetchAttrBinning = (role, params) =>
  get(
    `/profile/${role}/getAttrBinning?profileId=${params.profileId}&clusterId=${params.clusterId}&attributeId=${params.attributeId}&binningNum=8`,
  );

export const fetchAttrComparison = (role, params) =>
  get(
    `/profile/${role}/getAttrComparison?profileId=${params.profileId}&clusterId=${params.clusterId}`,
  );
