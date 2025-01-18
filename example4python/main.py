from collections import deque, defaultdict

'''
n表示有几个节点, edges是[][2]的数组, 表示边
'''
def contains_loop(n, edges):
    # defaultdict生成一个dict, 如果没有key, 那么会根据传入的构造方法自动生成对应的value
    adjs = defaultdict(list)  # 邻接表，默认为空列表
    indegrees = [0] * n       # 入度数组
    visited = [False] * n     # 访问标记数组

    # 初始化邻接表和入度数组
    for edge in edges:
        from_node, to_node = edge
        adjs[from_node].append(to_node)
        indegrees[to_node] += 1 # 将to_node的入度+1

    # 广度优先遍历
    q = deque() # 双端队列
    for i in range(n):
        if indegrees[i] == 0:
            # 如果入度为0, 那么是根节点
            q.append(i)

    while q:
        node = q.popleft()
        visited[node] = True
        # 找到根节点的所有子节点, 将入度-1, 并设置visited[node]设置为true
        for neighbor in adjs[node]:
            indegrees[neighbor] -= 1  # 更新入度
            if indegrees[neighbor] == 0:
                q.append(neighbor)

    # 检查是否有未访问的节点
    for i in range(n):
        if not visited[i]:
            return True  # 如果有未访问的节点，说明存在环

    return False  # 如果所有节点都被访问了，说明没有环

# 测试代码
def test_contains_loop():
    # 示例1: 无环图
    n1 = 4
    edges1 = [
        [0, 1],
        [0, 2],
        [1, 3],
        [2, 3]
    ]
    print("图1 是否有环:", contains_loop(n1, edges1))  # 应该输出: False

    # 示例2: 有环图
    n2 = 4
    edges2 = [
        [0, 1],
        [1, 2],
        [2, 0],
        [2, 3]
    ]
    print("图2 是否有环:", contains_loop(n2, edges2))  # 应该输出: True

# 运行测试代码
test_contains_loop()
