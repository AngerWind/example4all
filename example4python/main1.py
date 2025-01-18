from collections import defaultdict


class Solution:
    def __init__(self):
        self.adjs = defaultdict(list)
        self.colors = {}

    def containsLoop(self, n, edges):
        # 初始化邻接表和颜色
        for i in range(n):
            # 涂成白色, 表示没有检测过
            self.colors[i] = 'WHITE'

        for edge in edges:
            from_node, to_node = edge
            self.adjs[from_node].append(to_node)

        # 通过深度优先遍历判断是否存在环
        for i in range(n):
            if self.colors[i] == 'WHITE':
                if self.dfsVisit(i):
                    return True
        return False

    def dfsVisit(self, node):
        # 涂层灰色, 表示正在检测
        self.colors[node] = 'GRAY'
        # 返回当前节点的后续节点
        for adj in self.adjs[node]:
            if self.colors[adj] == 'GRAY':  # 检测到环
                return True
            if self.colors[adj] == 'WHITE':  # 继续DFS
                if self.dfsVisit(adj):
                    return True
        # 检查结束了, 涂层黑色
        self.colors[node] = 'BLACK'
        return False


# 测试示例
# 节点数为 4，边集如下：
edges = [
    [0, 1],
    [1, 2],
    [2, 3],
    [3, 1]  # 这条边形成了一个环：1 -> 2 -> 3 -> 1
]

solution = Solution()
n = 4  # 节点数量

print(solution.containsLoop(n, edges))  # 输出: True (因为存在环)
