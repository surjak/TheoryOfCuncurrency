from graphviz import Digraph
import itertools


def dependent_relations(alphabet, independent_relations):
    return sorted({pair for pair in itertools.product(alphabet, alphabet) if pair not in independent_relations})


def trace(word):
    return f"[<{','.join([letter for letter in word])}>]"


def FNF(alphabet, independent_relations, word):
    stacks = create_stacks(alphabet, independent_relations, word)
    max_size = max_stack_size(stacks)
    result = []

    for i in range(max_size):
        letters = pop_from_stacks(stacks)
        result.append(letters)
        optimize_FNF(result, independent_relations)

    return result, FNF_as_str(result)

def optimize_FNF(FNF, independent_vars):
    if len(FNF) < 2:
        return FNF
    last_foata_class = FNF.pop()
    next_to_last_foata_class = FNF.pop()
    # [('a', '1'),('b', '2')] <- item
    if all((item[0][0], item[1][0]) in independent_vars for item in itertools.product(last_foata_class, next_to_last_foata_class)):
        FNF.append(next_to_last_foata_class + last_foata_class)
    else :
        FNF.append(next_to_last_foata_class)
        FNF.append(last_foata_class)
    
def create_stacks(alphabet, independent_relations, word):
    stacks = {letter : [] for letter in alphabet}
    index = len(word)
    for letter in word[::-1]:
        stacks[letter].append((letter, str(index)))
        index-=1
        for alphabet_letter in alphabet:
            if alphabet_letter is not letter and (letter, alphabet_letter) not in independent_relations:
                stacks[alphabet_letter].append("*")
    return stacks


def max_stack_size(stacks):
    return max([len(value) for value in stacks.values()])


def pop_from_stacks(stacks):
    poped_letters = [stacks.get(letter).pop() for letter in stacks if len(stacks.get(letter))>0]
    set_poped = set(poped_letters)
    if "*" in set_poped:
        set_poped.remove("*")
    return sorted(list(set_poped))


def draw_graph(fnf, independent_relations, word):
    dot = Digraph()

    for foata_class in fnf:
        for item in foata_class:
            dot.node(item[1], item[0])

    # to check if two nodes are connected indirectly
    all_edges = []
    edges = []
    for i in range(len(fnf) - 2, -1, -1): # for foata class without the last one
        for u in fnf[i]: # for item in foata class
            for j in range(i + 1, len(fnf)): # for remaining foata classes
                for v in fnf[j]: # for item in remaining foata class
                    if (u[0], v[0]) not in independent_relations and (u, v) not in all_edges: # if two nodes are dependent and they aren't connected directly or indirectly
                        all_edges.append((u, v))
                        add_remaining_edges(all_edges, u, v)
                        edges.append((u, v))
                        dot.edge(u[1], v[1])
    print(dot.source)
    return edges


def add_remaining_edges(all_edges, u, v):
    for edge in all_edges:
        if edge[0] == v:
            all_edges.append((u, edge[1]))


def FNF_as_str(FNF):
    fnf = ''
    for foata_class in FNF:
        fnf += f"""({"".join([i[0] for i in foata_class])})"""
    return fnf


def FNF_from_graph(graph):
    nodes = get_nodes_from_graph(graph)
    graph = fill_fraph_with_all_connected_edges(graph, nodes)
    result = ''
    processed_nodes = []
    for i in range(len(nodes)):
        u = nodes[i]
        if u not in processed_nodes:
            processed_nodes.append(u)
            result += '('
            result += u[0]
            for j in range(i + 1, len(nodes)): # for remaining nodes
                v = nodes[j]
                if (u, v) not in graph and v not in processed_nodes:
                    result += v[0]
                    processed_nodes.append(v)
            result += ')'

    return result


def get_nodes_from_graph(graph):
    flat_list = [item for sublist in graph for item in sublist]
    nodes = list(set(flat_list))
    return sorted(nodes, key=lambda x: x[1])


def fill_fraph_with_all_connected_edges(graph, nodes):
    for node in nodes[::-1]: # for all nodes
        for u in graph: # for edges
            if node == u[0]: #if source of edge eq current node
                for v in graph: # for edges
                    if u != v and u[1] == v[0] and (node, v[1]) not in graph: # if the edge is different to the previous one and the destination of the previous one eq the source og the current one and the edge isn't in graph
                        graph.append((node, v[1]))

    return sorted(graph, key=lambda x: x[0][1])


def calculate(alphabet, independent_relations, word):
    print("Alphabet: ", sorted(alphabet))
    print("I: ", sorted(independent_relations, key=lambda x: x[0]))
    print("D: ", sorted(dependent_relations(alphabet, independent_relations), key=lambda x: x[0]))
    print("Word: ", word)
    print("[w]: ", trace(word))
    result, fnf = FNF(alphabet, independent_relations, word)
    print("Foata Normal Form: ", fnf)
    print("Diekert-Mazurkiewicz Graph:")
    edges = draw_graph(result, independent_relations, word)
    print("FNF based on graph: ", FNF_from_graph(edges), "\n")


# ex 1
print("-------------------------------------------------------------")
print("Example 1")
print("-------------------------------------------------------------")
A = {'a', 'b', 'c', 'd'}
I = {('a', 'd'), ('d', 'a'), ('b', 'c'), ('c', 'b')}
w = 'baadcb'
calculate(A, I, w)
print("-------------------------------------------------------------")
print("Example 2")
print("-------------------------------------------------------------")
# ex 2
A = {'a', 'b', 'c', 'd', 'e', 'f'}
I = {('a', 'd'), ('d', 'a'), ('b', 'e'), ('e', 'b'), ('c', 'd'), ('d', 'c'), ('c', 'f'), ('f', 'c')}
w = 'acdcfbbe'
calculate(A, I, w)
print("-------------------------------------------------------------")
print("Example 3")
print("-------------------------------------------------------------")
# ex 3
A = {'a', 'b', 'c', 'd', 'e'}
I = {('b', 'd'), ('d', 'b'), ('b', 'e'), ('e', 'b')}
w = 'acebdac'
calculate(A, I, w)
print("-------------------------------------------------------------")
