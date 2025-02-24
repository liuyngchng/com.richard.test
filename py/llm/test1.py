import langgraph as lg
from langgraph.graph import StateGraph
from langgraph.models import State, Transition
import graphviz
 
# 创建状态图
sg = StateGraph()
 
# 添加状态
state1 = sg.add_state(State(name="State1"))
state2 = sg.add_state(State(name="State2"))
state3 = sg.add_state(State(name="State3"))
 
# 添加转换（转移）
sg.add_transition(Transition(start=state1, end=state2, label="Transition1"))
sg.add_transition(Transition(start=state2, end=state3, label="Transition2"))
sg.add_transition(Transition(start=state3, end=state1, label="Transition3"))
 
# 使用Graphviz渲染并保存图形
dot = sg.to_dot()  # 将状态图转换为Graphviz的DOT格式
graphviz.Source(dot).render("state_graph", format='png', cleanup=True)  # 保存为PNG文件
