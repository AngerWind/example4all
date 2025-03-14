#### 关于flex的理解
当父元素设置为flex时, 子元素被从左到右排列
当设置了flex-wrap: wrap的时候, 并且子元素宽度大于父元素, 那么进行换行
形成多行, 每一行都有一个主轴, 当前行的高度是当前行最高的元素的高度

当设置flex-wrap: nowrap的时候, 子元素被压缩, 因为flex-grow默认为0, flex-shrink默认为1

当元素只有一行的时候, 可以通过align-items来控制元素在垂直方向上的对齐方式

![image-20230617033218695](img/readme/image-20230617033218695.png)

当元素有多行的时候, 可以通过align-content来控制多行在垂直方向上的对齐方式

下面代码分别样式align-content去不同值的时候的样式

~~~html
<body>
    <div style="width: 100vw;
            height: 100vh;
            display: flex;
            flex-wrap: wrap;
            /* align-items: center; */
            align-content: flex-start;
            flex: 1 1 auto;">
      <div style="width: 33%; flex: auto; background-color: blue; height: 20vh">1</div>
      <div style="width: 33%; flex: auto; background-color: green; height: 26vh">2</div>
      <div style="width: 33%; flex: auto; background-color: red; height: 14vh">3</div>
      <div style="width: 33%; flex: auto; background-color: yellow; height: 12vh">4</div>
      <div style="width: 33%; flex: auto; background-color: pink; height: 26vh">5</div>
      <div style="width: 33%; flex: auto; background-color: aqua; height: 39vh">6</div>
      <div style="width: 33%; flex: auto; background-color: violet; height: 20vh">7</div>
      <div style="width: 33%; flex: auto; background-color: violet; height: 15vh">7</div>
      <div style="width: 33%; flex: auto; background-color: violet; height: 11vh">7</div>
    </div>
  </body>
~~~



flex-start: 多行作为一个整体, 向上对齐

<img src="img/readme/image-20230617034316069.png" alt="image-20230617034316069" style="zoom: 25%;" />

flex-start: 多行作为一个整体, 向下对齐

<img src="img/readme/image-20230617034420766.png" alt="image-20230617034420766" style="zoom:25%;" />

center: 多行作为一个整体居中

![image-20230617034544227](img/readme/image-20230617034544227.png)

space-between: 空白平均分布在行之间

<img src="img/readme/image-20230617034746045.png" alt="image-20230617034746045" style="zoom:25%;" />

space-around: 空白平均分布在每一行的上下两边

<img src="img/readme/image-20230617035102322.png" alt="image-20230617035102322" style="zoom:25%;" />