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


## Secondmodel
* onclickevent를 통해 keyevent송수신(V)
* 한글 오토마타 기능 완성(이중모음, 이중자음)(V)
* 한글패드, 영문패드, 숫자패드, 특수기호패드 기능 추가(V)
* Key입력 시 소리 또는 진동 추가(사용자가 커스텀 가능하도록)(V)
* Long Click 입력 시 글자 변경 기능 추가(변경되는 기호는 미정)
* 키보드뷰 상단에 자주 쓰는 키패드를 추가할 수 있는 기능(텍스트 붙여넣기, 이모티콘, 커서이동 등등)(V)
* 천지인 자판, 이모티콘(V), 키보드 크기 조정(V)


### 1. Service
> - currentInputConnection: 현재 커서를 담고 있는 인덱스를 알고 추가, 변경, 삭제기능을 담당.
> - 현재 입력 상태에 따라 FrameLayout을 통해 KeyboardKorean, KeyboardEnglish, KeyboardSimbols, KeyboardNumpad, KeyboardEmoji로 변경된다.
> - 입력상태를 변환하기 위한 KeyboardInteractionListener 구현
> - 키보드의 높이를 정의하는 KeyboardSettingListener 구현
> - 사용자가 커스텀한 View를 추가하는 부분이 추가됨.
> - 한손조작모드 기능 추가

> #### KeyboardAction
> - 모든 종류의 키보드에서 구현되는 keyboard format layout
> - 키보드의 종류에 따라 정의된 Text로 구현됨

### 2. HangulMaker
> - 한글 오토마타를 정의하기 위한 Class
> - 4가지의 상태를 가지고 다음 입력값을 판단, commit한다.
> - 상태2, 상태3의 경우 이중모음 입력, 이중자음 입력을 판단하여 Text를 완성시킨다.


### 3. KeyboardView & Keyboard
> - API level 29 부터 deprecate되기 때문에 삭제한다.

### 4. MainActivity
> #### SettingDetailActivity
> - 소리 및 진동 설정 조절 가능
> - 키보드 크기 설정 조절 가능

> #### SettingCustomActivity
> - 편의 기능 설정
> - 한손 조작 모드
> - 사용자 설정 키 패드 추가
> - 두가지 설정을 한 Activity에서 조절 가능하도록 구현
> - SQL Database 추가(selectable item, selected item)

### 5. 이모티콘 기능 추가
> - KeyboardSimbols 또는 사용자 설정에서 설정 가능
> - 5자리 16진수로 이루어져 있기 때문에 Char형으로 두자리를 차지한다.
> - emoji를 참고하여 원하는 이모티콘에서 최대 n개까지의 인덱스를 통하여 이모티콘을 매핑했기 때문에 관련 이모티콘끼리 모여있다고 확신할 수 없다.(하드매핑 필요)
> - 이모티콘을 delete하는 연산을 수행할 경우 하나의 charSequence만 삭제하기 때문에 다른 문자로 변환된다.(수정 요망)
### 6. Automata
> - firstmodel의 경우 "", "ㅂ", "바", "박", "ㅏ" 의 다섯가지 상태를 가짐.
> - 각자의 상태에서 기대되는 다음 입력에 따라 상태가 변함.
> - 이중모음, 이중자음은 Flag를 통하여 처리
![ex_screenshot](./img/keyboardAutomata.PNG)

