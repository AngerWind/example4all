package main

import (
	"fmt"
	"github.com/tebeka/selenium"
	"github.com/tebeka/selenium/chrome"
	"os/exec"
	"regexp"
	"strings"
	"time"
)

func getMinimaIp() string {
	output, _ := CmdRun("ping incentivecash.minima.global")
	regstr := `\d+\.\d+\.\d+\.\d+`   // 两个及两个以上空格的正则表达式
	reg, _ := regexp.Compile(regstr) // 编译正则表达式

	return string(reg.Find([]byte(output)))
}
func CmdRun(cmd string) (string, error) {
	args := append([]string{"/C"}, cmd) // 向cmd开头添加元素
	command := exec.Command("cmd", args...)
	output, err := command.Output()
	return string(output), err
}

type Container struct {
	Id          string // 容器ID
	Name        string // 容器名称
	Status      string // 容器状态
	HostPort    uint   // 宿主机的端口
	Port        uint   // 容器的端口
	IncentiveId string // todo
}

func (c Container) PauseCmd() string {
	return fmt.Sprintf("docker pause %s", c.Id)
}
func (c Container) ExecPause() (string, error) {
	return CmdRun(c.PauseCmd())
}
func (c Container) UnpauseCmd() string {
	return fmt.Sprintf("docker unpause %s", c.Id)
}
func (c Container) ExecUnpause() (string, error) {
	return CmdRun(c.UnpauseCmd())
}

// 将minima的地址加入到容器的hosts中
func (c Container) AddHosts(ip string) ([]byte, error) {
	// cmd := fmt.Sprintf(`docker exec %s /bin/bash -c ""echo -e '\nincentivecash.minima.global %s\n' >> /etc/hosts""`, c.Id, getMinimaIp())
	// println(cmd)
	// return CmdRun(cmd)

	args := []string{
		"/C",
		"docker",
		"exec",
		c.Id,
		"/bin/bash",
		"-c",
		fmt.Sprintf(`""echo -e '\n%s incentivecash.minima.global\n' >> /etc/hosts""`, ip),
	}
	command := exec.Command("cmd", args...)
	return command.Output()
}

type IncentiveInfo struct {
	Email       string    // 邮箱
	Phone       string    // 手机号
	IncentiveId string    // 奖励ID
	Amount      float64   // 奖励数量
	UpdateTime  time.Time // 上次更新时间
}
type Info struct {
	Container
	IncentiveInfo
}

const (
	seleniumPath = `.\chromedriver.exe`
	port         = 19515
)

// func update(Container c ) {
// 	// docker
//
//
// }

func newChromeDriverService() *selenium.Service {
	opts := []selenium.ServiceOption{}
	// selenium.SetDebug(true)
	service, err := selenium.NewChromeDriverService(seleniumPath, port, opts...)
	if nil != err {
		panic(err)
	}
	return service
}
func getWebDriver() selenium.WebDriver {

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
			"--incognito",
		},
	}
	// 以上是设置浏览器参数
	caps.AddChrome(chromeCaps)

	// 调起chrome浏览器
	webDriver, err := selenium.NewRemote(caps, fmt.Sprintf("http://localhost:%d/wd/hub", port))
	if err != nil {
		panic(err)
	}
	return webDriver

}
func main() {
	service := newChromeDriverService()
	defer service.Stop()

	driver := getWebDriver()
	defer driver.Close()

	ip := getMinimaIp()

	for _, container := range Containers {
		container.ExecUnpause()
		container.AddHosts(ip)
		defer container.ExecPause()

		driver.Get(fmt.Sprintf("https://localhost:%d", container.HostPort))
		if ele, err := driver.FindElement(selenium.ByID, "details-button"); err == nil {
			ele.Click() // 点击高级
			time.Sleep(100 * time.Millisecond)
			continueBtn, _ := driver.FindElement(selenium.ByID, "proceed-link")
			continueBtn.Click() // 点击继续前往
		}
		pwdInput, _ := driver.FindElement(selenium.ByXPATH, "/html/body/div[2]/ul/center/div/form/input[2]")
		pwdInput.SendKeys("123")

		pwdBtn, _ := driver.FindElement(selenium.ByXPATH, "/html/body/div[2]/ul/center/div/form/input[3]")
		pwdBtn.Click()

		mainmenu, _ := driver.FindElement(selenium.ByXPATH, "/html/body/div[2]/ul/center[1]/div/form/input[2]")
		mainmenu.Click()

		incentiveProgram, _ := driver.FindElement(selenium.ByXPATH, "/html/body/div[2]/ul/li[4]/a")
		incentiveProgram.Click()

		handles, _ := driver.WindowHandles()
		driver.SwitchWindow(handles[len(handles)-1])
		time.Sleep(1000 * time.Millisecond)

		for {
			time.Sleep(500 * time.Millisecond)
			element, _ := driver.FindElement(selenium.ByID, "uid")
			element.Clear()
			element.Clear()
			element.SendKeys(" ")

			driver.ExecuteScript(fmt.Sprintf(`document.querySelector("#uid").value = '%s'`, container.IncentiveId), []interface{}{})
			element.SendKeys(" ")
			time.Sleep(1000 * time.Millisecond)
			incentiveUpdate, _ := driver.FindElement(selenium.ByXPATH, "//*[@id=\"root\"]/div/main/div/div[2]/div/form/div/button")
			incentiveUpdate.Click()
			time.Sleep(500 * time.Millisecond)

			driver.WaitWithTimeout(func(wd selenium.WebDriver) (bool, error) {
				attr, _ := incentiveUpdate.GetAttribute("className")
				if strings.Contains(attr, "Mui-disabled") {
					return false, nil
				}
				return true, nil
			}, 10*time.Second)
			successSpan, _ := driver.FindElement(selenium.ByCSSSelector, "#root > div > main > div > div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-xs-10.MuiGrid-grid-sm-9.MuiGrid-grid-lg-6.css-1jgmrmj > div > form > div > span")
			if successSpan != nil {
				break
			}
		}

	}

}
