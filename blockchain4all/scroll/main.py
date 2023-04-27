import os
import time

from selenium import webdriver
from selenium.webdriver import ChromeOptions
from selenium.webdriver.common.by import By


def init_web_driver(chrome_data_dir):
    options = ChromeOptions()
    options.add_experimental_option("excludeSwitches", ["enable automation"])
    options.add_experimental_option("useAutomationExtension", False)  # 屏蔽webdriver特性
    options.add_argument("--disable-blink-features")
    options.add_argument("--disable-blink-features-AutomationControlled")
    # 加载chrome的用户文件, 包括插件, 用户cookie
    # 用户的配置文件中要先加载插件, 配置账户地址, 添加scroll  alpha Test 网络
    options.add_argument("--user-data-dir-" + chrome_data_dir)
    driver = webdriver.Chrome(options=options, executable_path="../driver/chromedriver.exe")
    return driver


def get_chrome_data_dir(dir_name):
    try:
        data_dir = os.path.abspath(".") + "\\chrome-user-data\\" + dir_name
        if not os.path.exists(data_dir):
            os.makedirs(data_dir)
        return data_dir
    except Exception as e:
        print(e)


# 获取指定元素的text
def get_text(css_class, attemp=10):
    cnt = 0
    while cnt < attemp:
        try:
            value = driver.find_element(By.CSS_SELECTOR, css_class).text
            return value
        except:
            time.sleep(2)
            cnt += 1
    return -1


def get_amount(attemp_cnt=10):
    global meta_handle
    driver.switch_to.window(meta_handle)
    return get_text('.currency-display-component__text', attemp_cnt)


def press_button(css_class, attemp_cnt=10):
    for i in range(0, attemp_cnt):
        try:
            driver.find_element(By.CSS_SELECTOR, css_class).click()
            time.sleep(2)
            return True
        except Exception as e:
            time.sleep(2)
            print(e)
    return False


def press_button_from_list(css, idx, cnt=10):
    for i in range(0, cnt):
        try:
            driver.find_elements(By.CSS_SELECTOR, css)[idx].click()
            time.sleep(2)
            return True
        except Exception as e:
            time.sleep(2)
            print(e)
    return False


def press_button_from_list_byText(css, btn_name, cnt=10):
    for i in range(0, cnt):
        try:
            list = driver.find_elements(By.CSS_SELECTOR, css)
            for btn in list:
                if btn.text == btn_name:
                    btn.click()
                    time.sleep(2)
                    return True
        except Exception as e:
            time.sleep(2)
            print(e)
    return False


def send_keys(css, key, cnt=10):
    for i in range(0, cnt):
        try:
            driver.find_element(By.CSS_SELECTOR, css).send_keys(key)
            return True
        except:
            time.sleep(2)
    return False


if __name__ == '__main__':
    driver = init_web_driver(get_chrome_data_dir("test1"))
    driver.get("https://scroll.io/prealpha/bridge")

    # 切换到刚刚打开的新空白窗口, 并跳转到小狐狸插件
    driver.execute_script("window.open('')")
    driver.get('chrome-extension://nkbihfbeogaeaoehlefnkodbefgpgknn/home.html')
    # 小狐狸网页handle
    meta_handle = driver.window_handles[1]
    # 主网页面handle
    main_handle = driver.window_handles[0]
    get_amount()

    # 由于交易需要gas fee, 我们可以通过token的变化来确认交易是否成功, 所以需要获取当前的token的数量
    # 下面这个需要网络是在 scroll alpha test 网络
