(function () {
	/* 기본 side_menu와 card 설정 */
	card_list_TagInput($('.side_menu > ul > li:eq(0)').attr('value'), $('.tabLocalList > li:eq(0)').attr('value'));
	$('.side_menu > ul > li').first().addClass('selected');


	var slide_index = 0;
	var local_index = 0;

	$(".tabLocalList li").on("click", function(){
		// 클릭하는 요소의 index 번호를 가져옴
		const num = $(".tabLocalList li").index($(this));
		// 기존에 적용되어 있는 selected class 삭제
		$(".tabLocalList li").removeClass("selected");
		// 클릭한 요소에 selected class 추가
		$('.tabLocalList li:eq(' + num + ')').addClass("selected");
		local_index = num;
		card_list_TagInput($('.side_menu > ul > li:eq(' + slide_index +')').attr('value'), $('.tabLocalList > li:eq('+ local_index +')').attr('value'));
	});

	/* side_manu : class에 selected 설정 => CSS파일참고 */

	const menuWrap = document.querySelector('.side_menu');

	function select(ulEl, liEl) {
		Array.from(ulEl.children).forEach((v) => v.classList.remove('selected'));
		if (liEl) liEl.classList.add('selected');
	}
	$('.side_menu').on('click', function (e) {
		/*  menuWrap.addEventListener('click', e => { */
		//function unselect_removeAtt() {
		$('.side_menu > ul > li').each(function (index, element) {
			if (element.classList.contains('selected') === true) {
				element.classList.remove('selected');
			}

			const selected = e.target;
			// unselect_removeAtt(menuWrap);
			select(menuWrap, selected);
		});
	});

	/* id="slide" 내부에 childNodes 가 있으면 삭제하고 card 생성 */

	$('.side_menu > ul > li').on('click', function (e) {
		var index = $('.side_menu > ul > li').index(this);
		var list = document.getElementById('slide');

		if (list.hasChildNodes()) {
			list.removeChild(list.childNodes[0]);
		}
		slide_index = index;
		card_list_TagInput($('.side_menu > ul > li:eq(' + slide_index +')').attr('value'), $('.tabLocalList > li:eq('+ local_index +')').attr('value'));
	});
})();
// card();
/* side_menu */

function card() {
	/* Card 부분 슬라이드 효과 */

	const slideList = document.querySelector('.slide_list'); // Slide parent dom
	const slideContents = document.querySelectorAll('.slide_content'); // each slide dom
	const pagination = document.querySelector('.slide_pagination');
	const slideLen = slideContents.length; // slide length
	const slideWidth = 1125; // slide width
	const slideSpeed = 200; // slide speed
	const startNum = 0; // initial slide index (0 ~ 4)

	slideList.style.width = slideWidth * (slideLen + 2) + 'px';

	let firstChild = slideList.firstElementChild;
	let lastChild = slideList.lastElementChild;
	try {
		let clonedFirst = firstChild.cloneNode(true);
		let clonedLast = lastChild.cloneNode(true);


	slideList.appendChild(clonedFirst);
	slideList.insertBefore(clonedLast, slideList.firstElementChild);



	let pageChild = '';
	for (var i = 0; i < slideLen; i++) {
		pageChild += '<li class="dot';
		pageChild += i === startNum ? ' dot_active' : '';
		pageChild += '" data-index="' + i + '"><a href="#"></a></li>';
	}
	pagination.innerHTML = pageChild;
	const pageDots = document.querySelectorAll('.dot'); // each dot from pagination

	slideList.style.transform = 'translate3d(-' + slideWidth * (startNum + 1) + 'px, 0px, 0px)';

	let curIndex = startNum; // current slide index (except copied slide)
	let curSlide = slideContents[curIndex]; // current slide dom
	curSlide.classList.add('slide_active');
	} catch {

	}
	/** Button Event */
	try {
	let curDot;
	Array.prototype.forEach.call(pageDots, function (dot, i) {
		dot.addEventListener('click', function (e) {
			e.preventDefault();
			curDot = document.querySelector('.dot_active');
			curDot.classList.remove('dot_active');

			curDot = this;
			this.classList.add('dot_active');

			curSlide.classList.remove('slide_active');
			curIndex = Number(this.getAttribute('data-index'));
			curSlide = slideContents[curIndex];
			curSlide.classList.add('slide_active');
			slideList.style.transition = slideSpeed + 'ms';
			slideList.style.transform = 'translate3d(-' + slideWidth * (curIndex + 1) + 'px, 0px, 0px)';
		});
	});
	} catch {

	}
}

function card_list_TagInput (tag, local){
	$.ajax({
		url : "/indexCard",
		type : 'post',
		data : {Tag : tag, Local : local},
		success : function(data) {
			card_data(data);
		},
	});
}



function card_data(ac_Datas) {

	/* 배열 사이즈 */
	var arr_size = ac_Datas.length;

	// console.log( Obj_val);
	/* dot 개수 설정 */
	var dot_count = ac_Datas.length / 8;

	/* id="slide" */
	var list = document.getElementById('slide');

	/* 기본 세팅 시작 태그 */
	var slide_start = `<div class="slide_container slide_menu">
                                <div class="slide_wrap">
                                    <div class="slide_box">
                                        <div class="slide_list clearfix">
                                    `;

	/* 기본 세팅 종료 태그 */
	var slide_end = `
                                      </div>
                                    </div>
                                <ul class="slide_pagination"></ul>
                                </div>
                            </div>
    `;

	/* 카드 내부 시작 태그 */
	var data_start = `<div class="slide_content card_container">`;

	/* 카드 내부 종료 태그 */
	var data_end = `</div>`;

	/* innerHTML insert 변수 */
	var data = ``;

	/* 기본 세팅 시작 태그 추가 */
	data += slide_start;

	/* dot 개수에 따른 태그생성(dot별로 8개씩 카드) */
	for (var dot = 0; dot < dot_count; dot++) {
		/* 카드 내부 시작 태그 추가*/
		data += data_start;

		/* dot 별로 카드 개수 설정 임시변수 */
		var arr_size_temp;

		/* dot 별로 카드 개수 설정 */

		/* 입력받은 카드가 8개 이상일 경우 dot에 카드 8개 세팅 */
		if (arr_size >= 8) {
			arr_size_temp = 8;
			/* 입력받은 카드가 8개 미만일 경우 해당 arr_size 세팅 */
		} else {
			arr_size_temp = arr_size;
		}

		/* dot 별로 카드 추가 */
		for (var dot_data = 0; dot_data < arr_size_temp; dot_data++) {
			var ac_Data = ac_Datas[dot_data+(dot*8)];
			var ac_evel = ac_Data[Object.keys(ac_Data)[5]];
			var star = ``;

			for(var star_i=0; star_i > ac_evel; star_i++)
			{
				star += `<i className="fa-solid fa-star"></i>`
			}
			data +=
				`<div class="item" OnClick="location.href ='/academy?code=` + ac_Data["academycode"] +`'" style="cursor:pointer;">` +
				`<table>
                        <tr id="images">
                <td><img src=""></td>
                
                        </tr>` +
				<!-- 학원명 출력 -->
				`<tr>
                           <td style="font-weight  : bold">` +
					ac_Data["academy"].name
			//	Object.value(ac_Data)
					+

				`</td>
                        </tr>` +

				<!-- region 출력 -->
				`<tr>
                            <td>` +
				ac_Data["academy"].region    +
				`</td>
                        </tr>` +

				<!-- eval (별점) 출력 -->
                `<tr>
                            <td>`;
				data+= star;
				data+= ` (` + ac_Data["academy"].eval + `) </td> 
                            <th>` + `</th>
 
                        </tr>` +
                        /*

                `<tr>
                            <td>` + test_arr[arr_size - (dot_data + 1)] + `</td>
                            <th>` + `</th>
                        </tr>` +
 
                /*
                `<tr>
                    <td>` + test_arr[arr_size - (dot_data + 1)] + `</td>
                    <th>` + `</th>
                    <th>` + `</th>
                </tr>` +
                */

				`</table> 
                </div>`;
		}

		/* dot 별로 카드 8개 이상일 경우 최대 사이즈 변경 // for문으로 통해 dot_data를 추가했기 때문에 -=8 한다. */
		if (arr_size >= 8) {
			arr_size -= 8;
		}

		/* 카드 내부 종료 태그 추가*/
		data += data_end;
	}

	/* 기본 세팅 종료 태그 추가 */
	data += slide_end;

	list.innerHTML = data;
	card();
}
