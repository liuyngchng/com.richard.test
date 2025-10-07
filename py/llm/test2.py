from langgraph.graph import StateGraph, START, END
from langgraph.graph.message import add_messages
from typing_extensions import TypedDict
from typing import Annotated
from graphviz import Digraph

# 定义图的状态信息
class State(TypedDict):
    # Messages have the type "list". The `add_messages` function
    # in the annotation defines how this state key should be updated
    # (in this case, it appends messages to the list, rather than overwriting them)
    messages: Annotated[list, add_messages]
        
def chatbot(state: State):
    return {"messages": ["this is a test"]}        

# 通过 graphviz 输出图
def export_graphviz(graph):
    dot = Digraph()
    # 添加节点
    for node in graph.nodes:
        dot.node(str(id(node)), str(node))  # 假设节点有name属性
    # 添加边
    for source, target in graph.edges:
        dot.edge(str(id(source)), str(id(target)))
    return dot


graph_builder = StateGraph(State)
# 定义图的入口和边
graph_builder.add_node("chatbot", chatbot)
graph_builder.add_edge(START, "chatbot")
graph_builder.add_edge("chatbot", END)

# 编译图
graph = graph_builder.compile()
# 生成graph.pdf
export_graphviz(graph.get_graph()).render("graph")
