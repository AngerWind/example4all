| | | | | | |
|-|-|-|-|-|-|
|选择器分类|选择器定义语法|作用|例子|例子说明|备注|
|通用选择器|*|选择所有元素|*|选择所有元素| |
|元素选择器|element|选择指定元素|div|选择所有div标签| |
|ID选择器|#id|选择指定ID|#username|选择id=username的元素| |
|class选择器|.class|选择class属性中包含指定类的元素|.color|选择所有class包含color的元素| |
|属性选择器|[attribute]|选择带有指定属性的元素|[title]|选择所有带title属性的元素|在使用属性选择器的时候, 需要在前面添加*或者其他选择器|
| |[attribute=value]|选择指定属性为指定属性值的元素, 需要完全匹配|*[class="color"]|选择所有class="color"的元素, 需要完全匹配| |
| |[attribute~=value]|选择属性中**包含**指定词汇的元素|*[class~="color"]|选择所有class包含"color"的元素, 与类选择器相同| |
| |[attribute"|=value]|属性以指定值开头的元素, 该值必须是一整个单词|*[lang"|="en"] |选择 lang 属性等于 en 或以 en- 开头的所有元素| |
| |[attribute^=value]|属性值以指定值开头的元素|*[abc^="def"] |选择 abc 属性值以 "def" 开头的所有元素 | |
| |[attribute$=value]|属性值以指定值结尾的元素|*[abc$="def"]  |选择 abc 属性值以 "def" 结尾的所有元素| |
| |[attribute*=value]|属性值包含指定值的元素|*[abc*="def"]  |选择 abc 属性值中包含子串 "def" 的所有元素| |
|后代选择器|ele ele|选择指定后代的元素|h1 em |选择h1中的em元素| |
|子元素选择器|ele > ele|选择指定子元素的元素|h1>em|选择h1的子元素em| |
|兄弟选择器|ele+ele|选择紧跟在指定元素后面的元素, 两者父元素相同|h1 + p|选择紧接在 h1 元素后出现的段落，h1 和 p 元素拥有共同的父元素| |
|伪类选择器|ele:pseudo-class |选择具有指定伪类的某个元素| | |伪类是指浏览器给符合条件的元素自动添加的隐藏class, 所有伪类如下:  点击过的a标签具有visited 伪类, 只能给a元素 没有点击过的a标签具有link伪类, 只能给a元素                              鼠标悬停的元素具有hover伪类, 可以给任意元素绑定 拥有键盘输入焦点的元素具有focus伪类 被激活的元素具有active伪类, 可以给任意元素绑定 作为某个元素第一个子元素的元素具有first-child伪类 作为某个元素最后一个子元素的元素具有last-child伪类 :not 反选器  即选择不具有伪类的选择器 带有lang属性的元素具有lang伪类 |
| | | |a:link |选择未访问的链接| |
| | | |a:visited |选择已访问的链接| |
| | | |a:hover|选择鼠标悬停的链接| |
| | | |a:active|选择被选择的链接| |
| | | |input:focus|选择具有输入焦点的input标签| |
| | | |div > p:first-child|选择div标签中的p标签, 且该p标签是第一个子标签| |
| | | |p:lang(no)|选择p标签, 且该p标签的lang="no"| |
| | | |ul li:not(:last-child)|选择ul下的li标签, 且li标签不是最后一个子标签| |
|伪元素选择器|ele::pseudo-ele| | | |伪元素即隐藏在特定位置的元素, 一般用来设置文本首行, 首字母的样式, 在指定元素内容前后添加内容 first-letter元素会隐含的包裹住向文本中第一个字母 first-line元素会隐含的包裹住文本中首行 before会被插入在元素内容的最开始, 结合content来设置before元素的内容, before默认是行内元素 after会被插入在元素内容的最后, 结合content来设置before元素的内容, after默认是行内元素 |
| | | |p::first-letter{   color:#ff0000; }|给p标签的第一个字母设置样式| |
| | | |p::first-letter{   color:#ff0000; }|给p标签的第一个行设置样式| |
| | | |p::before{   content:"&emps; &emps; "; }|给p标签文本前添加两个空格| |
| | | |p::after{   content:"&emps; &emps; "; }|给p标签文本后添加两个空格| |
| | | | | | |
|选择器分组|selector, selector| |div, p|选择所有div和p元素| |
