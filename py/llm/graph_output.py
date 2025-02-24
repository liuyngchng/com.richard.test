import graphviz
filename="graphviz.dot"
outname="Test"
#画图
with open(filename,encoding='utf-8') as f:
    dot_graph = f.read()
graph=graphviz.Source(dot_graph);
graph.render(outname, format='jpg', view=False)
