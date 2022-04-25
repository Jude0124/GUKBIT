(function () {
      function loadEvent() {
        const form = document.querySelector('form');
        form.addEventListener('submit', function (evt) {
              const keyword = document.querySelector('#search-box');
              if (!keyword.value || keyword.value == "" || keyword.value == null
                  || (typeof keyword.value == "object" && !Object.keys(
                      keyword.value).length)) {
                // alert('검색할 학원명을 입력해주세요.');
                Swal.fire({
                  icon: 'warning',
                  html: '검색할 학원명을 입력해주세요.',
                  showConfirmButton: false,
                  timer: 1200
                })
                keyword.focus();
                keyword.value = '';
                evt.preventDefault();           // 제약 조건에 만족하지 않으면 전송 기능을 OFF
                return;                         // 다음 체크로 넘어가는 걸 방지
              }
            }
        )
      };
      window.addEventListener('load', loadEvent)
    }
)
();