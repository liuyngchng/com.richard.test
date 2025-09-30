from graphviz import Digraph

dot = Digraph(name="MyPicture", comment="the test", format="png")


dot.node(name='a', label='wo', color='purple')
dot.node(name='b', label='niu', color='purple')
dot.node(name='c', label='che', color='purple')

dot.edge('a', 'b', label='woniu', color='red')
dot.edge('b', 'c', label='woniuche', color='red')

dot.render('checheche', view=False)
