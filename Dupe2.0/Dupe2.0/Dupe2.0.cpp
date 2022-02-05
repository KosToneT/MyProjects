#include <iostream>
#include <windows.h>
#include <thread>

class FPS {
    int MAX_FPS = 30;
    int FPS_NOW;
    time_t start;
public:
    void control() {
        int ping = 100;
        time_t curr = clock();
        if (curr - start > ping) {
            start = curr;
            FPS_NOW = 0;
        }
        FPS_NOW++;
        if (FPS_NOW > MAX_FPS) {
            Sleep(start + ping - curr);
        }

    }
};

class Mouse {
public:
    void clickLeftMouse(int time=40) {
        INPUT inputs[1] = {};
        ZeroMemory(inputs, sizeof(inputs));
        inputs[0].type = INPUT_MOUSE;
        inputs[0].mi.dwFlags = MOUSEEVENTF_LEFTDOWN;
        SendInput(ARRAYSIZE(inputs), inputs, sizeof(INPUT));
        Sleep(time);
        inputs[0].mi.dwFlags = MOUSEEVENTF_LEFTUP;
        SendInput(ARRAYSIZE(inputs), inputs, sizeof(INPUT));
    }
    void clickRightMouse(int time=40) {
        INPUT inputs[1] = {};
        ZeroMemory(inputs, sizeof(inputs));
        inputs[0].type = INPUT_MOUSE;
        inputs[0].mi.dwFlags = MOUSEEVENTF_RIGHTDOWN;
        SendInput(ARRAYSIZE(inputs), inputs, sizeof(INPUT));
        Sleep(time);
        inputs[0].mi.dwFlags = MOUSEEVENTF_RIGHTUP;
        SendInput(ARRAYSIZE(inputs), inputs, sizeof(INPUT));
    }
    void pressButton(byte key, int time = 40) {
        INPUT inputs[1] = {};
        ZeroMemory(inputs, sizeof(inputs));
        inputs[0].type = INPUT_MOUSE;
        inputs[0].mi.dwFlags = MOUSEEVENTF_LEFTDOWN;
        SendInput(ARRAYSIZE(inputs), inputs, sizeof(INPUT));
        Sleep(time);
        inputs[0].mi.dwFlags = MOUSEEVENTF_LEFTUP;
        SendInput(ARRAYSIZE(inputs), inputs, sizeof(INPUT));
    }
};
void pressButton(byte key, int time =40) {
    INPUT inputs[1] = {};
    ZeroMemory(inputs, sizeof(inputs));
    inputs[0].type = INPUT_KEYBOARD;
    inputs[0].ki.wVk = key;
    SendInput(ARRAYSIZE(inputs), inputs, sizeof(INPUT));
    Sleep(time);
    inputs[0].ki.dwFlags = KEYEVENTF_KEYUP;
    SendInput(ARRAYSIZE(inputs), inputs, sizeof(INPUT));
}
class Settings{
public:
    bool attack = false;
    bool prank = false;
    byte key = 0x5A;
};
Settings settings;

void update() {
    system("cls");
    system("color 2");

    printf("attack=%d\n", settings.attack);
    printf("prank=%d\n", settings.prank);
    printf("bind=0x%X\n", settings.key);
}


void Command() {
    std::string str = "";
    while (true) {
        update();
        std::cin >> str;
        if (str == "attack") {
            settings.attack = !settings.attack;
            std::cout << "attack=" << settings.attack << std::endl;
        }
        else if (str == "prank") {
            settings.prank = !settings.prank;
            std::cout << "prank=" << settings.prank << std::endl;
        }
        else if (str == "bind") {
            Sleep(1000);
            bool run = true;
            while (run) {
                for (int i = 0; i < 0xFF; i++) {
                    if (GetAsyncKeyState(i)&0x8000) {
                        run = false;
                        settings.key = i;
                    }
                }
            }
        }
    }

}

int main(){
    std::thread th  = std::thread(Command);
    th.detach();
    Mouse m = Mouse();
    FPS fps = FPS();
    while (true) {
        if (settings.attack) {
            if (GetAsyncKeyState(VK_XBUTTON2) & 0x8000) {
                m.clickLeftMouse();
                Sleep(40);
            }
        }
        if (settings.prank) {
            if (GetAsyncKeyState(VK_XBUTTON2) & 0x8000) {
                pressButton(settings.key);
                Sleep(40);
                pressButton(0xD);
                Sleep(40);
            }
        }
        fps.control();
    }
    return 0;
}