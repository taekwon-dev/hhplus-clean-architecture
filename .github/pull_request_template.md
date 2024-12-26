### **주요 커밋 링크**

- [Step3]
  - 신청 가능한 특강 목록 조회 서비스 구현 : b601d5c
  - 특강 신청 서비스 구현 : d71b8cd
  - 신청 완료 특강 목록 조회 서비스 구현 : ddaebb3
  - 신청 가능한 특강 목록 조회 API 구현 : 35bbdb0
  - 특강 신청 API 구현 : 1212a8b
  - 신청 완료 특강 목록 조회 API 구현 : 4ac280c
  - 선착순 30명 이후의 신청자의 경우 실패하도록 개선 : 6fabb53
  - README.md 문서 작성 : 274df81

- [Step4]
  - 같은 사용자가 동일한 특강에 대해 신청 성공하지 못하도록 개선 : e559518


---
### **리뷰 포인트(질문)**
- #### 리뷰 포인트 1 : Stub 을 이용한 동시성 실패 테스트 해보기 관련 질문
  - 테스트 환경에서 Stub을 활용해서 동시성 제어를 하지 않은 조건을 만들고, 동시성 이슈가 발생하는지를 한 번 만들어보고 싶었는데, 결과적으로 성공하지는 못했습니다. 의도했던 것은 실제 테스트 DB 환경에서 비관적 락을 사용하지 않은 상태로 구현한 코드가 동작했을 때 동시성 이슈가 발생하는지 검증해보고 싶었습니다. 
- ```java 
    public interface ScheduleJpaRepository extends JpaRepository<Schedule, Long> {     
        @Lock(value = LockModeType.PESSIMISTIC_WRITE)
        Optional<Schedule> findById(long id);
    }
    
    // 1) 제 생각에는 우선 위 JpaRepository 구현체를 대체해야 한다고 생각해서 아래와 같이 생성했고,   
    public interface FakeScheduleJpaRepository extends JpaRepository<Schedule, Long> {
        Optional<Schedule> findById(long id); // Spring Data JPA 에서 지원하는 추상 메서드라 따로 코드 작성할 필요 없음 (이해 돕기 위해 작성)      
    }
  
    @Repository    
    @ActiveProfiles("test")
    public class FakeScheduleCoreRepository implements ScheduleRepository {

        private final FakeScheduleJpaRepository fakeJpaRepository;
  
        public FakeScheduleCoreRepository(FakeScheduleJpaRepository fakeJpaRepository) {
            this.fakeJpaRepository = fakeJpaRepository;
        }   

        @Override
        public Schedule save(Schedule schedule) {
           // ... 
        }

        @Override
        public List<Schedule> findAvailableSchedules(long userId, LocalDateTime gracePeriodDate) {
            // ... 
        }

        @Override
        public Schedule findById(long id) {
            return fakeJpaRepository.findById(id).orElseThrow(ScheduleNotFoundException::new);
        }
    }
  
    // 2) 아래 처럼 각각 의존성을 주입 받아 테스트를 하려고 했는데, 실패한 상황입니다. (빈 주입 단계에서부터 에러가 나는 상황)
    class ScheduleServiceTest extends ServiceTest {
    
        @Autowired
        @Qualifier("scheduleCoreRepository")        
        private ScheduleRepository scheduleRepository; // 비관적 락 적용
  
        @Autowired
        @Qualifier("fakeScheduleCoreRepository")     
        private ScheduleRepository fakeScheduleRepository; // 비관적 락 적용 X 
    }
  ```
- 질문1: 우선 제가 접근한 방식으로 하는 게 적절할까요? 아예 잘못된 방향으로 접근한 것인지 궁금합니다.! (만약 위와 같이 접근하는 게 맞다면, 빈 주입 에러가 발생하는 단계부터 한 번 다시 풀어보려고 합니다!)
- 질문2: 혹시 적절한 방식이 아니라면, 어떤 식으로 Stub을 이용해 동시성 실패 케이스를 테스트 할 수 있는지 데모 또는 설명 부탁드리고 싶습니다.! 감사합니다. 
 
---
### **이번주 KPT 회고**

### Keep
- 팀원 분들과의 스크럼을 진행헀다.
- 다루지 못한 관련 주제를 기록했다.

### Problem
- 오랜만의 Spring Framework 사용이라 미숙한 게 느껴졌다. 기본기를 틈틈히 채우자. 
- 시간 관리, 주어진 시간 안에서 해결 할 수 있는 것에 집중하자.
- 면접, 코테 일정이 있어 몰아서 하게 됐다. 루틴을 유지할 수 있도록 개선하자!

### Try
- 작업 단위를 조금 더 미리 계획하고 나눠보자.