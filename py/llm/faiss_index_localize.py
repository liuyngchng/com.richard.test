#! /usr/bin/python3
from langchain_community.document_loaders import TextLoader
# 加载 word文档
from langchain_unstructured import UnstructuredLoader 
from langchain_text_splitters import CharacterTextSplitter
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_community.vectorstores import FAISS
from langchain.chains import RetrievalQA

import logging
import logging.config
 
# 加载配置
logging.config.fileConfig('logging.conf')
 
# 创建 logger
logger = logging.getLogger("root")
file = "./1.pdf"
# 加载知识库文件
logger.info("load local doc {}".format(file))
loader = UnstructuredLoader(file)
#loader = TextLoader(file,encoding='utf8')
documents = loader.load()

# 将文档分割成块
logger.info("split doc")
text_splitter = CharacterTextSplitter(chunk_size=1000, chunk_overlap=0)
texts = text_splitter.split_documents(documents)

# 加载Embedding模型，进行自然语言处理
logger.info("load embedding model")
embeddings = HuggingFaceEmbeddings(model_name="../bge-large-zh-v1.5", cache_folder='./bge-cache')

# 创建向量数据库
logger.info("build vector db")
db = FAISS.from_documents(texts, embeddings)
# 保存向量存储库至本地，save_local() 方法将生成的索引文件保存到本地，以便之后可以重新加载
logger.info("save vector db to local file")
db.save_local("./faiss_index")
logger.info("vector db saved to local file")
