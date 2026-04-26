# -*- coding:utf-8 -*-
# @author  : shenfei
# @time    : 2025/4/7 16:53
# @function: python后端调用
import uvicorn
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from summarize import summarize
from cluster import cluster
from classification import classification
from academic_cluster import academic_cluster
from agent_chat import agent_router
from idg_prof import idg_prof

app = FastAPI()

@app.get("/health")
def health_check():
    return {"status": "healthy", "message": "Python service is running"}


# 更完整的CORS配置
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 允许所有来源
    allow_credentials=True,
    allow_methods=["*"],  # 允许所有方法
    allow_headers=["*"],  # 允许所有头部
)

app.include_router(summarize, prefix='/summarize')
app.include_router(cluster, prefix='/cluster')
app.include_router(classification, prefix='/classification')
app.include_router(academic_cluster, prefix='/academic')
app.include_router(idg_prof, prefix='/idg-prof')
app.include_router(agent_router, prefix='/agent')

# academic
if __name__ == '__main__':
    uvicorn.run('main:app', port=8009, host="0.0.0.0")
