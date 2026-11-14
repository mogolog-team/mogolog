# 공통 화면 틀과 하단 탭 (A3)

## 화면 구성

- `activity_main.xml`: 위쪽 `fragment_container`와 아래쪽 `bottom_navigation`.
- `res/menu/bottom_nav.xml`: 홈 / 기록 / 커뮤니티 메뉴.
- 스크린샷처럼 흰색 하단 바, 상단 구분선, 둥근 사각형 아이콘을 사용합니다.
- 선택 탭은 `primary`, 미선택 아이콘은 `line`, 미선택 글자는 `text_hint`입니다.
- 하단 바의 콘텐츠 높이는 64dp이며 시스템 내비게이션 영역만큼 높이와 하단 여백을 추가합니다.
  상태 표시줄과 화면 좌우의 안전 영역은 Activity 루트에서 처리합니다.

## 담당자별 수정 위치

모든 클래스의 패키지는 기존과 동일한 `com.team.mogolog`입니다.

| 탭 | Fragment | 레이아웃 | 담당자 |
| --- | --- | --- | --- |
| 홈 | `HomeFragment.java` | `fragment_home.xml` | 안재한 |
| 기록 | `ExamListFragment.java` | `fragment_exam_list.xml` | 이상민 |
| 커뮤니티 | `CommunityFragment.java` | `fragment_community.xml` | 이서준 |

각 Fragment는 제목만 표시합니다. 제목은 스크린샷에 맞춰 모고로그 / 시험 기록 / 커뮤니티로
설정했습니다. 이후 담당자가 해당 Fragment와 레이아웃을 채우면 됩니다.
공통 `TextTitle`, `screen_padding` 리소스를 사용하고 배경은 앱 테마의 `bg`를 상속합니다.

## 탭 전환과 복원

- 처음 실행하면 홈을 표시합니다.
- Fragment는 최초 방문 시 추가하고, 탭 이동 시 숨기거나 다시 표시합니다.
  탭을 다시 눌러도 Fragment를 중복 생성하지 않습니다.
- 숨긴 탭은 최대 `STARTED`, 표시 중인 탭은 `RESUMED` 상태입니다.
  담당자는 현재 화면에서만 수행할 동작을 `RESUMED` 수명주기에 맞춰 연결할 수 있습니다.
- 선택한 탭 ID는 Activity 상태에 저장합니다. 화면 재생성 시 FragmentManager가 복원한
  Fragment를 재사용하고 선택한 탭과 표시 화면을 맞춥니다.
- 하단 탭 이동은 뒤로 가기 기록에 쌓지 않습니다.
- Activity에서 시스템 영역 여백을 처리하므로 Fragment에서 같은 여백을 다시 추가하지 않습니다.

## 확인 방법

`MainNavigationTest`는 첫 홈 표시, 세 탭 전환, 재선택, 이전 탭 숨김,
Activity 재생성 후 선택 탭과 화면 복원을 검증합니다.
현재 프로젝트의 Espresso 3.5.1은 API 36에서 입력 초기화 오류가 발생하므로,
추가 테스트는 의존성 변경 없이 Android 기본 `UiAutomation`으로 터치 이벤트를 주입합니다.

```powershell
.\gradlew.bat :app:assembleDebug :app:lintDebug
.\gradlew.bat :app:connectedDebugAndroidTest
```

연결 테스트는 실행 중인 에뮬레이터 또는 테스트 기기가 필요합니다.
Gradle 설정과 Manifest는 이 작업에서 변경하지 않습니다.
