function navigo() {
	const menuheight = $('.wrapped-header').outerHeight(); //선택한 요소의 height값
	document.addEventListener('scroll', onScroll, { passive: true });
	//passive : true 스크롤 성능 향상 / preventDefault를 사용하여 해당 이벤트를 막을 수 없다.

	function onScroll() {
		const scrollposition = scrollY;
		if (menuheight <= scrollposition) {
			//현재의 scroll위치가 menu의 height보다 더 크면
			$('#nav-wrapper').addClass('fix');
		} else {
			$('#nav-wrapper').removeClass('fix');
		}
	}
}
navigo();
