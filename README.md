# Keyboard-Study
안드로이드 키보드 모델에 대한 학습자료

## Firstmodel 
* android.inputmethodservice.Keyboard 라이브러리(API 29버전에서 분리됨)
* 다른 커스텀 레이아웃의 keyevent사용 필요

### InputMethodService
> - currentInputConnection: 현재 커서를 담고 있는 인덱스를 알고 추가, 변경, 삭제기능을 담당.
> - key event를 감지하여 휴대폰과 Connection을 담당

### Service
> - service로 등록하여 다른 애플리케이션이 실행중일 경우에도 사용이 가능하다.
> - 발생하는 KeyEvent를 감지하고 정의된 동작에 따라 EditText의 변경을 담당한다.

### KeyboardView
> - OnKeyboardActionListener를 상속하여 KeyboardView의 onKey메소드 재정의.
> - 키보드를 변경해야할 경우 KeyboardView의 속성인 keyboard를 변경하면 된다.

### Keyboard
> - xml에 정의된 key_layout을 의미함.
> - 한글, 영어로 정의되어 있고, 영어 유니코드를 기반으로 설정하여 한글의 경우 mapping과정을 통하여 변환되는 과정을 거침.(ex. 'q'->'ㅂ')

### Automata
> - firstmodel의 경우 "", "ㅂ", "바", "박", "ㅏ" 의 다섯가지 상태를 가짐.
> - 각자의 상태에서 기대되는 다음 입력에 따라 상태가 변함.
![ex_screenshot](./img/keyboardAutomata.PNG)

## Secondmodel
* onclickevent를 통해 keyevent송수신
* 한글 오토마타 기능 완성(이중모음, 이중자음)
* 한글패드, 영문패드, 숫자패드, 특수기호패드 기능 추가
* Key입력 시 소리 또는 진동 추가(사용자가 커스텀 가능하도록)
* Long Click 입력 시 글자 변경 기능 추가(변경되는 기호는 미정)
* 키보드뷰 상단에 자주 쓰는 키패드를 추가할 수 있는 기능(텍스트 붙여넣기, 이모티콘, 커서이동 등등)
* 천지인 자판, 이모티콘, 키보드 크기 조정


### Service
> - currentInputConnection: 현재 커서를 담고 있는 인덱스를 알고 추가, 변경, 삭제기능을 담당.
> - KeyEvent를 KeyboardView객체가 아닌 Instrumentation객체가 송신하고 버튼의 ClickListener를 통하여 트리거한다.
> - KeyboardView의 OnKeyboardActionListener가 아닌 서비스 자체의 OnKeyDown메소드를 통하거나, 키보드 전체의 뷰를 담당하는 LinearLayout(Constraint Layout등으로 대체 가능)의 OnKeyListener를 통하여 KeyEvent를 감지할 수 있다.

### KeyboardView & Keyboard
> - API level 29 부터 deprecate되기 때문에 삭제한다.

### TIL 및 향후 방향
 #### 2019.08.20
> - 진동 기능 추가(사용자 커스텀 아직)
> - 커스텀 전용 뷰 추가
> - 특수문자 패드 추가

#### 2019.08.21
> - 진동 세기 조절 커스텀 가능
> - 소리 On/Off 조절 가능(소리 크기는 사용자의 휴대폰 크기에 비례하는 것으로 추정)

#### 2019.08.27
> - 키보드 높이를 조절할 수 있도록 변경
> - 테마 변경 기능 추가 필요

#### 2019.08.28
> - 한글 오토마타 완성(이중모음, 이중자음)
> - 이중모음 DELETE기능 보완 필요(두개의 모음이 한번에 지워지는 현상 해결)

 #### 목표
 > - 소리 및 진동 커스텀(~2019. 08. 21)
 > - 키보드 크기 조절(~2019. 08. 26)
 > - 오토마타 완성(~2019. 08. 28)
 > - 편의기능 추가(자주쓰는 키패드)(~2019. 09. 01)
 > - 키보드 자판 추가(~2019. 09. 08)
 > - 키보드 테마 추가(~2019. 09. 11)
 > - 개선 및 에러 수정(~2019. 09. 20)
