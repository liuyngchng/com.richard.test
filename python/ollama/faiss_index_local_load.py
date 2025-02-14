#! /usr/bin/python3

from langchain_community.vectorstores import FAISS
from langchain.chains import RetrievalQA
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_ollama import OllamaLLM

import logging
import logging.config

# 加载配置
logging.config.fileConfig('logging.conf')

# 创建 logger
logger = logging.getLogger()

# for test purpose only, read index from local file
embeddings = HuggingFaceEmbeddings(model_name="../bge-large-zh-v1.5", cache_folder='./bge-cache')
logger.info("try to load index from local file")
loaded_index = FAISS.load_local('./faiss_index', embeddings, allow_dangerous_deserialization=True)
logger.info("load index from local file finish")

# 创建远程 Ollama API代理
logger.info("get remote llm agent")
llm = OllamaLLM(model="deepseekR1:7B", base_url='http://127.0.0.1:11435')
#llm = OllamaLLM(model="llama2:7B", base_url='http://127.0.0.1:11435')

# 创建检索问答链
logger.info("build retrieval")
qa = RetrievalQA.from_chain_type(llm=llm, chain_type="stuff", retriever=loaded_index.as_retriever())

# 提问
query = "一个秘密问题，你知道吗？"
logger.info("invoke retrieval")
result = qa.invoke(query)
logger.info(result)
