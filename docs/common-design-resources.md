# 공통 디자인 리소스 (A12-1~4)

## 기준

- 요구사항의 색상, 화면 여백 20dp, 카드 모서리 16dp, 버튼 모서리 12dp,
  글자 크기 20sp / 14sp / 12sp를 사용합니다.
- 배지 색상은 제공된 시험 기록 스크린샷(142204)에서 추출했습니다.
- 입력칸 모서리 10dp, 배지 모서리 6dp, 테두리 1dp는 스크린샷을 참고한 값입니다.
  원본 Figma 수치가 확인되면 `dimens.xml`에서 조정합니다.
- 제공된 화면은 밝은 테마이므로 시스템 야간 모드에서도 동일한 팔레트를 사용합니다.
  별도의 어두운 팔레트는 정의하지 않았습니다.
- `build.gradle.kts`, `AndroidManifest.xml` 및 기존 화면 코드는 수정하지 않았습니다.

## 색상과 크기

화면에서는 직접 색 코드를 쓰지 않고 `@color/primary`, `@color/text_main`,
`@color/text_sub`, `@color/text_hint`, `@color/line`, `@color/bg`, `@color/card`를 사용합니다.

| 용도 | 리소스 | 값 |
| --- | --- | --- |
| 화면 여백 | `@dimen/screen_padding` | 20dp |
| 카드 모서리 | `@dimen/card_corner_radius` | 16dp |
| 버튼 모서리 | `@dimen/button_corner_radius` | 12dp |
| 제목 / 본문 / 라벨 | `@dimen/text_size_title` / `text_size_body` / `text_size_label` | 20sp / 14sp / 12sp |

## 글자 스타일

```xml
<TextView
    style="@style/TextTitle"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/app_name" />
```

- `TextTitle`: 20sp, 굵게, `text_main`
- `TextBody`: 14sp, 보통, `text_main`
- `TextLabel`: 12sp, 굵게, `text_hint`
- 보조 본문은 `TextBody`에 `android:textColor="@color/text_sub"`를 추가합니다.

## 배경

| 용도 | 배경 |
| --- | --- |
| 카드 | `@drawable/bg_card` |
| 입력칸 | `@drawable/bg_input` |
| 기본 버튼 | `@drawable/bg_button_primary` |
| 과목 선택 칩 | `@drawable/bg_chip` |

배경은 모양만 정의합니다. 내용 여백과 컴포넌트 높이는 화면에서 지정합니다.
기본 버튼의 글자색은 `@color/card`를 사용합니다.
AppCompatButton이나 EditText의 기본 tint가 공통 배경색을 덮어쓰는 경우
`app:backgroundTint="@null"`을 지정합니다.
MaterialButton / Material Chip처럼 자체 배경을 관리하는 컴포넌트는
직접 배경을 덮어쓰기보다 해당 컴포넌트의 tint·stroke·모서리 속성에 공통 리소스를 연결합니다.

## 칩 선택 상태

일반 TextView를 칩으로 사용하는 예시입니다. `subject_label`은 화면의 문자열 리소스입니다.

```xml
<TextView
    android:id="@+id/subject_chip"
    style="@style/TextBody"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:background="@drawable/bg_chip"
    android:clickable="true"
    android:focusable="true"
    android:text="@string/subject_label"
    android:textColor="@color/chip_text" />
```

실제 화면에서는 칩의 패딩과 터치 영역도 지정합니다.
`bg_chip`과 `chip_text`는 `state_selected`와 `state_checked`를 모두 지원합니다.

```java
// 단일 선택 그룹은 화면 담당자가 나머지 칩의 선택을 해제합니다.
subjectChip.setSelected(true);  // 파란 배경 + 흰 글자
subjectChip.setSelected(false); // 흰 배경 + 회색 글자 + 테두리
```

selector 자체가 클릭 시 선택을 변경하지는 않습니다. 클릭 리스너에서 상태를 갱신해야 합니다.
체크 가능한 View에서는 `setChecked(...)`를 사용할 수 있습니다.

## 시험 구분 배지

| 구분 | 배경 | 글자색 | 배경색 / 글자색 값 |
| --- | --- | --- | --- |
| 평가원 | `@drawable/bg_badge_kice` | `@color/badge_kice_text` | #E7ECFF / #3B5BDB |
| 교육청 | `@drawable/bg_badge_education` | `@color/badge_education_text` | #E3F7EE / #138A5E |
| 사설 | `@drawable/bg_badge_private` | `@color/badge_private_text` | #FFF1E0 / #C2620A |

`TextLabel`을 적용한 TextView에 배경과 해당 글자색을 함께 지정합니다.
리소스 이름을 바꾸기 전에는 팀원에게 공유합니다.
