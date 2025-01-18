print("root的__init__文件被执行")

# 使用绝对路径
import root.sub1, root.sub2, root.c

# 或者使用相对路径
from . import c, sub1, sub2
