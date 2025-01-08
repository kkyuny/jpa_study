# Ch04. Entity 기본 속성 살펴보기

## 1. IL
- 도메인 패키지에서 Entity의 여러 속성에 대해 학습.
- ID를 생성하는 4가지 방식에 대해 이해:
  - **GenerationType**
    1. **IDENTITY**: MySQL, MariaDB 등에서 사용. 트랜잭션 종료 전에 ID 값을 미리 받아와 사용.
    2. **SEQUENCE**: Oracle, PostgreSQL 등에서 사용. Insert 시 `Sequence` 증가 쿼리 실행.
    3. **TABLE**: DB 종류에 상관없이 ID 값을 관리하는 별도의 테이블을 두어 ID를 추출하여 사용.
    4. **AUTO**: 기본값. 각 DB에 적합한 방식을 자동으로 선택.
- Enum에 대해서도 간략히 학습.

---

## 2. 트러블슈팅
### 문제
- `Enum` 컬럼에서 `@Enumerated(value = EnumType.STRING)` 설정이 없을 경우, Enum 순서대로 매핑됨.
- Enum 순서 변경 시, DB 값과 매핑이 어긋날 위험 존재.

### 해결
- `@Enumerated(value = EnumType.STRING)` 설정을 추가하면 Enum 값이 순서에 상관없이 DB와 정확히 매핑되어 문제 해결 가능.

---

## 3. 추가 정리
### Enum 관련 트러블슈팅 사례

#### 1. 초기 Enum 선언
```
public enum Gender {
  MALE,
  FEMALE
}
```
DB 저장 결과
- `MALE = 1`
- `FEMALE = 2`

---

#### 2. 중간에 새로운 값 추가
```
public enum Gender {
  MALE,
  UNKNOWN,
  FEMALE
}
```
변경 후, DB 값 매핑:
- 기존 `FEMALE = 2` → `FEMALE = 3`
- `UNKNOWN = 2`로 매핑됨.

문제
- DB의 기존 값은 변경되지 않아 기존의 FEMALE(2)이 UNKOWN(2)에 맵핑되는 데이터 불일치 발생.

---

#### 3. 문제 해결 방법

`@Enumerated(value = EnumType.STRING)` 설정을 사용.
```
@Entity
public class User {
  @Enumerated(value = EnumType.STRING)
  private Gender gender;
}
```
결과: Enum의 값이 DB에 숫자가 아닌 실제 문자열대로 저장되어 맵핑되지 않는 문제를 해결할 수 있다.
