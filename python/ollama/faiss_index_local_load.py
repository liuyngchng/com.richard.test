from langchain.vectorstores import FAISS
from langchain.chains import RetrievalQA
from langchain.embeddings import HuggingFaceEmbeddings
from langchain_community.llms import Ollama


# for test purpose only, read index from local file
embeddings = HuggingFaceEmbeddings(model_name="../bge-large-zh-v1.5", cache_folder='./bge-cache')
print("try to load index from local file")
loaded_index = FAISS.load_local('./faiss_index', embeddings, allow_dangerous_deserialization=True)
print("load index from local file finish")

# 创建远程 Ollama API代理
llm = Ollama(model="deepseekR1:7B", base_url='http://11.10.36.1:11435')

# 创建检索问答链
qa = RetrievalQA.from_chain_type(llm=llm, chain_type="stuff", retriever=loaded_index.as_retriever())

# 提问
query = "投保人是否可以变更?？"
result = qa.run(query)
print(result)
