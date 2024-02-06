# RecipeSpace
요리 레시피 공유 앱

## 개요
나의 요리 레시피를 다른 사람들과 공유함으로써 요리를 할 때 많은 도움을 받을 수 있는 앱입니다.<br>
SeoultechRecipe를 Kotlin으로 리팩토링했습니다.
    

## 프로젝트 기간
2022\. 11 ~ 2023. 02


## 기여도
- 기획, 개발, 디자인 모두 했습니다.


## 사용 프로그램 및 언어
- 사용 프로그램 : Android Studio, Google Firebase, GitHub
- 사용 언어 : Kotlin


## 앱의 버전
- minSdkVersion : 23
- targetSdkVersion : 34


## 이용 대상
- 기숙사 생활/자취를 하는 학생들
- 1인 가구
- 요리에 관심이 많은 누구나


## 주요 기능
- 사용자들은 앱에 자신만의 요리 레시피를 피드에 업로드하여 공유할 수 있다.

- 채팅을 통해 레시피 관련 대화를 할 수 있다.

- 피드에 있는 레시피에 대해 평가를 할 수 있다.


## 사용된 기술
- MVVM 패턴
- 안드로이드 클린 아키텍처
- Firebase Firestore
- Room
- ViewPager2
- RecyclerView
- Coroutine
- LiveData
- Hilt
- CameraX
- SSOT (Single Source Of Truth)


## 사용된 라이브러리
- Material 디자인
- 사진 첨부 (Glide)
- ElasticView (com.github.skydoves:elasticviews)


## 개발 후 느낀 점
Kotlin으로 마이그레이션함으로써 Java에는 없는 Kotlin만의 장점이 무엇인지를 알 수 있었습니다. <br>
기존에 적용해본 적 없는 SSOT 방식으로 DB를 구축해보면서 DB 구축 방식에 대해 고민해볼 수 있는 좋은 개발 기회였습니다. <br>

## 스크린샷
<img src="/images/splash.png" width="150px" height="320px" title="Splash" alt="Splash"></img>
<img src="/images/login.png" width="150px" height="320px" title="Login" alt="Login"></img>
<img src="/images/sign_up.png" width="150px" height="320px" title="SignUp" alt="SignUp"></img>
<img src="/images/recipe_feed.png" width="150px" height="320px" title="RecipeFeed" alt="RecipeFeed"></img>
<img src="/images/recipe_detail.png" width="150px" height="320px" title="RecipeDetail" alt="RecipeDetail"></img>
<img src="/images/recipe_rating.png" width="150px" height="320px" title="Rating" alt="Rating"></img>
<img src="/images/post_recipe.png" width="150px" height="320px" title="Upload" alt="Upload"></img>
<img src="/images/chat_list.png" width="150px" height="320px" title="ChatList" alt="ChatList"></img>
<img src="/images/chat_room.png" width="150px" height="320px" title="ChattingRoom" alt="ChattingRoom"></img>
<img src="/images/account_profile.png" width="150px" height="320px" title="Profile" alt="Profile"></img>
<img src="/images/edit_profile.png" width="150px" height="320px" title="ProfileEdit" alt="ProfileEdit"></img>
<img src="/images/setting.png" width="150px" height="320px" title="Setting" alt="Setting"></img>


# 목표
- Google PlayStore 런칭
