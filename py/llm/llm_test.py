#!/usr/bin/python3
from langchain.document_loaders import TextLoader
from langchain.text_splitter import CharacterTextSplitter
from langchain.embeddings import HuggingFaceEmbeddings
from langchain.vectorstores import FAISS
from langchain.chains import RetrievalQA
from langchain_community.llms import Ollama
import warnings

 # 忽略所有FutureWarning警告
warnings.filterwarnings("ignore")


# 加载知识库文件 insurance.txt
loader = TextLoader("knowledge.txt",encoding='utf8')
documents = loader.load()

# 将文档分割成块
text_splitter = CharacterTextSplitter(chunk_size=1000, chunk_overlap=0)
texts = text_splitter.split_documents(documents)

# 加载Embedding模型，进行自然语言处理
embeddings = HuggingFaceEmbeddings(model_name="bge-large-zh-v1.5")

# 创建向量数据库
db = FAISS.from_documents(texts, embeddings)

# 创建本地deepseek LLM
llm = Ollama(model="deepseekR1:7B", base_url="http://11.10.36.1:11435")

# 创建检索问答链
qa = RetrievalQA.from_chain_type(llm=llm, chain_type="stuff", retriever=db.as_retriever())

# 提问
query = "这种药物有什么功效？"
result = qa.run(query)
print(result)
