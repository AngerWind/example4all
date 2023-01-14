package main

import (
	"fmt"
	"os/exec"
	"testing"
)

func TestUnpause(t *testing.T) {
	output, err := CmdRun("docker unpause d0f8b0eb837c19cef183094004f3408bb566eda5b005bae5672dcd4ee5dae856")
	// output, err := CmdRun("dir")
	if err != nil {

	}
	fmt.Println(output)
}
func TestPause(t *testing.T) {
	output, err := CmdRun("docker pause d0f8b0eb837c19cef183094004f3408bb566eda5b005bae5672dcd4ee5dae856")
	// output, err := CmdRun("dir")
	if err != nil {

	}
	fmt.Println(output)
}

func TestPin(t *testing.T) {
	// output, _ := CmdRun("ping incentivecash.minima.global")
	// regstr := `\d+\.\d+\.\d+\.\d+`   // 两个及两个以上空格的正则表达式
	// reg, _ := regexp.Compile(regstr) // 编译正则表达式
	//
	// ip := reg.Find([]byte(output))
	// fmt.Printf(string(ip))

	// s := fmt.Sprintf(`docker exec -it %s /bin/bash -c "echo -e '\nincentivecash.minima.global %s\n' >> /etc/hosts"`, "minima", getMinimaIp())
	// println(s)

	_, err := Containers[0].AddHosts(getMinimaIp())
	if err != nil {

	}

}

func TestPin1(t *testing.T) {
	// docker exec d0f8b0eb837c19cef183094004f3408bb566eda5b005bae5672dcd4ee5dae856 /bin/bash -c "echo -e '\nincentivecash.minima.global 34.89.121.227\n' >> /etc/hosts"
	args := []string{
		"/C",
		"docker",
		"exec",
		"d0f8b0eb837c19cef183094004f3408bb566eda5b005bae5672dcd4ee5dae856",
		"/bin/bash",
		"-c",
		`"echo -e '\nincentivecash.minima.global 34.89.121.227\n' >> /etc/hosts"`,
	}
	command := exec.Command("cmd", args...)
	_, err := command.Output()
	if err != nil {

	}

}
