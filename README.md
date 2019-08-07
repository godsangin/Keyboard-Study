# Keyboard-Study
안드로이드 키보드 모델에 대한 학습자료

# Firstmodel 
* android.inputmethodservice.Keyboard 라이브러리(API 29버전에서 분리됨)
* 다른 커스텀 레이아웃의 keyevent사용 필요

## InputMethodService
> - currentInputConnection: 현재 커서를 담고 있는 인덱스를 알고 추가, 변경, 삭제기능을 담당.
> - key event를 감지하여 휴대폰과 Connection을 담당.

## KeyboardView
> - OnKeyboardActionListener를 상속하여 KeyboardView의 onKey메소드 재정의.
> - 키보드를 변경해야할 경우 KeyboardView의 속성인 keyboard를 변경하면 된다.

## Keyboard
> - xml에 정의된 key_layout을 의미함.
> - 한글, 영어로 정의되어 있고, 영어 유니코드를 기반으로 설정하여 한글의 경우 mapping과정을 통하여 변환되는 과정을 거침.(ex. 'q'->'ㅂ')

## Automata
> - firstmodel의 경우 4가지 상태로 "", "ㅂ", "바", "박"의 네가지 상태를 가짐.
> - 각자의 상태에서 기대되는 다음 입력에 따라 상태가 변함.
![ex_screenshot](./img/keyboardAutomata.png)

# Secondmodel
* 다른 뷰 레이아웃에서 keyevent사용 가능성 확인
* 한글 오토마타 기능 완성(이중모음, 이중자음)
* 자동완성 기능 추가
