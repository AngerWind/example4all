package main

import (
	"fmt"
	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
	"testing"
	"time"
)

func TestSelenium(t *testing.T) {
	// 如果seleniumServer没有启动，就启动一个seleniumServer所需要的参数，可以为空，示例请参见https://github.com/tebeka/selenium/blob/master/example_test.go
	opts := []selenium.ServiceOption{}
	// opts := []selenium.ServiceOption{
	//    selenium.StartFrameBuffer(),           // Start an X frame buffer for the browser to run in.
	//    selenium.GeckoDriver(geckoDriverPath), // Specify the path to GeckoDriver in order to use Firefox.
	// }

	// selenium.SetDebug(true)
	service, err := selenium.NewChromeDriverService(seleniumPath, port, opts...)
	if nil != err {
		fmt.Println("start a chromedriver service falid", err.Error())
		return
	}
	// 注意这里，server关闭之后，chrome窗口也会关闭
	defer service.Stop()

	// 链接本地的浏览器 chrome
	caps := selenium.Capabilities{
		"browserName": "chrome",
	}

	// 禁止图片加载，加快渲染速度
	imagCaps := map[string]interface{}{
		"profile.managed_default_content_settings.images": 2,
	}
	chromeCaps := chrome.Capabilities{
		Prefs: imagCaps,
		Path:  "",
		Args: []string{
			// "--headless", // 设置Chrome无头模式，在linux下运行，需要设置这个参数，否则会报错
			// "--no-sandbox",
			"--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36", // 模拟user-agent，防反爬
		},
	}
	// 以上是设置浏览器参数
	caps.AddChrome(chromeCaps)

	// 调起chrome浏览器
	webDriver, err := selenium.NewRemote(caps, fmt.Sprintf("http://localhost:%d/wd/hub", port))
	if err != nil {
		fmt.Println("connect to the webDriver faild", err.Error())
		return
	}
	// 关闭一个webDriver会对应关闭一个chrome窗口
	// 但是不会导致seleniumServer关闭
	defer webDriver.Quit()
	err = webDriver.Get("https://www.baidu.com")
	if err != nil {
		fmt.Println("get page faild", err.Error())
		return
	}

	// 重新调起chrome浏览器
	w_b2, err := selenium.NewRemote(caps, fmt.Sprintf("http://localhost:%d/wd/hub", port))
	if err != nil {
		fmt.Println("connect to the webDriver faild", err.Error())
		return
	}
	defer w_b2.Close()
	// 打开一个网页
	err = w_b2.Get("https://www.toutiao.com/")
	if err != nil {
		fmt.Println("get page faild", err.Error())
		return
	}
	// 打开一个网页
	err = w_b2.Get("https://studygolang.com/static/pkgdoc/pkg/archive_tar.htm#example-package")
	if err != nil {
		fmt.Println("get page faild", err.Error())
		return
	}
	// w_b就是当前页面的对象，通过该对象可以操作当前页面了
	// ........

	// 查找页面中的搜索框
	btn, err := w_b2.FindElement(selenium.ByID, "kw")
	if err != nil {
		// panic(err)
		fmt.Println(err)
	}
	// 向搜索框中输入  “你好”
	err = btn.SendKeys("你好")
	if err != nil {
		// panic(err)
		fmt.Println(err)
	}
	// 查找提交按钮
	btn1, err := w_b2.FindElement(selenium.ByID, "su")
	if err != nil {
		// panic(err)
		fmt.Println(err)
	}
	// 点击提交按钮
	if err := btn1.Click(); err != nil {
		// panic(err)
		fmt.Println(err)
	}

	// 获取sessionid
	getsessionid := w_b2.SessionID()
	fmt.Println(getsessionid)

	// 返回当前页面连接的url
	geturl, err := w_b2.CurrentURL()
	fmt.Println(geturl)

	// 获取当前页面的所有内容
	getpagesource, err := w_b2.PageSource()
	fmt.Println(getpagesource)

	time.Sleep(5 * time.Minute)
	return
}
