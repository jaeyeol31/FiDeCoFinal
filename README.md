![스크린샷 2024-08-06 오후 1 04 00](https://github.com/user-attachments/assets/84effe85-7bea-48af-8960-006384c9532f)
# 프로젝트 개요

이 프로젝트는 사용자가 브랜드별, 옵션별로 다양한 컴퓨터 제품을 비교하여 최적의 선택을 할 수 있도록 돕는 컴퓨터 쇼핑몰 사이트입니다. 사용자의 요구와 예산에 맞는 컴퓨터를 추천하며, 사용자가 원하는 디자인의 컴퓨터도 추천합니다. Spring Framework를 중심으로 최신 기술 스택을 활용하여, 실무에서의 확장성과 유지보수성을 고려한 시스템을 구축하였습니다. 이 프로젝트는 사용자에게 최상의 쇼핑 경험을 제공하고, 시스템의 성능과 안정성을 보장합니다.

## 인원
   2명


## 담당 기능

- **회원가입**: 사용자 이메일 인증을 통해 신뢰할 수 있는 회원 정보를 관리합니다.
- **로그인**: 기본적인 로그인 기능과 아이디, 비밀번호 찾기, 간편 로그인을 제공합니다.
- **상품**: 다양한 컴퓨터 제품을 브랜드별, 옵션별로 비교할 수 있는 기능을 제공합니다.
- **장바구니**: 사용자가 선택한 제품을 장바구니에 추가하고 관리할 수 있습니다.
- **주문**: 사용자 주문 처리를 통해 구매 과정을 지원합니다.
- **관리자 페이지**: 관리자 권한을 통해 시스템 전체를 관리하고, 상품, 회원, 주문 등의 CRUD 기능을 제공합니다.

## 주요 기술 및 기능

1. **이메일 인증**:
   - **설명**: 사용자 이메일을 확인하여 신뢰할 수 있는 회원 정보를 관리합니다.
   - **기술**: 구글 이메일 서비스와 스프링 이메일 라이브러리 사용.

2. **카카오 주소 API**:
   - **설명**: 사용자가 주소를 입력할 때 편리하게 입력할 수 있도록 지원합니다.
   - **기술**: 카카오 주소 API 통합.

3. **OAuth 2.0**:
   - **설명**: 네이버, 카카오톡 API를 이용한 간편 로그인을 통해 사용자가 자신의 계정 정보를 공유하지 않고도 다른 서비스에 대한 접근 권한을 안전하게 제공합니다.
   - **기술**: 스프링 시큐리티와 OAuth 2.0을 사용한 소셜 로그인 구현.

4. **스프링 시큐리티**:
   - **설명**: 인증 및 인가 관리를 통해 시스템 보안을 강화합니다.
   - **기술**: 스프링 시큐리티를 사용한 사용자 인증 및 권한 관리.

5. **JWT**:
   - **설명**: 스프링 시큐리티와 JWT를 사용한 토큰 기반 인증을 통해 무상태 세션을 유지하여 확장성과 가용성을 높입니다.
   - **기술**: JWT를 사용한 토큰 기반 인증 구현.

6. **CRUD 기능**:
   - **설명**: 상품, 회원, 장바구니, 주문, 결제 조회 등의 기본적인 CRUD 기능을 제공합니다.
   - **기술**: Spring Data JPA와 Oracle 21을 사용한 데이터베이스 연동.

7. **관리자 페이지**:
   - **설명**: 관리자 권한을 통해 시스템 전체를 관리합니다.
   - **기능**: 관리자 페이지에서 상품, 회원, 주문 등의 CRUD 기능을 관리할 수 있습니다.

## 프로젝트의 장점

- **확장성**: MSA 구조와 JWT를 사용하여 서버 확장과 장애 복구가 용이합니다.
- **보안성**: 스프링 시큐리티와 OAuth 2.0을 통해 안전한 인증 및 권한 관리를 제공합니다.
- **사용자 편의성**: 이메일 인증, 간편 로그인, 카카오 주소 API 통합 등을 통해 사용자 경험을 향상시킵니다.
- **관리 효율성**: 관리자 페이지를 통해 시스템을 효율적으로 관리할 수 있습니다.
<img width="1608" alt="스크린샷 2024-08-11 오후 8 44 53" src="https://github.com/user-attachments/assets/eb566343-a144-4b54-852b-4a65463a1d05">
<img width="1608" alt="스크린샷 2024-08-11 오후 8 45 06" src="https://github.com/user-attachments/assets/b6ea2829-1ecc-4087-9b6b-0127ca54ba20">
<img width="1608" alt="스크린샷 2024-08-11 오후 8 45 09" src="https://github.com/user-attachments/assets/9a0deaba-e831-482a-a44f-b7eab7500410">
<img width="1608" alt="스크린샷 2024-08-11 오후 8 45 12" src="https://github.com/user-attachments/assets/5d26ddf1-17c2-4deb-b7a7-9173c7d2d107">
<img width="1608" alt="스크린샷 2024-08-11 오후 8 45 15" src="https://github.com/user-attachments/assets/b2009c32-8782-4dea-bb57-197f1a80ffa5">
<img width="1608" alt="스크린샷 2024-08-11 오후 8 45 17" src="https://github.com/user-attachments/assets/9ea2c2b7-013a-4e88-aa12-2292c13db37e">
<img width="1608" alt="스크린샷 2024-08-11 오후 8 45 23" src="https://github.com/user-attachments/assets/16aaf5c4-4f06-457f-b8da-c5011cb0134f">
