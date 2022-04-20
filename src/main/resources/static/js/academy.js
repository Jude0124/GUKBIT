(function () {
	console.log(expectedSelect);

	$('#acd_cour_page').css('display', 'none');

	const academymenu = document.querySelector('.academy_middle');

	$('.academy_middle').on('click', function (e) {
		/*  menuWrap.addEventListener('click', e => { */
		//function unselect_removeAtt() {
		$('.academy_middle ul li').each(function (index, element) {
			if (element.classList.contains('selected') === true) {
				element.classList.remove('selected');
			}
		});
		const selected = e.target;
		// unselect_removeAtt(menuWrap);
		select(academymenu, selected);
	});

	function select(ulEl, liEl) {
		Array.from(ulEl.children).forEach((v) => v.classList.remove('selected'));
		if (liEl) liEl.classList.add('selected');
	}

	$('.academy_middle ul li').on('click', function (e) {
		var index = $('.academy_middle ul li').index(this);

		$('#acd_review_page').css('display', 'none');
		$('#acd_cour_page').css('display', 'none');

		switch (index) {
			case 0:
				$('#acd_review_page').css('display', 'block');
				break;

			case 1:
				$('#acd_cour_page').css('display', 'block');
				break;

			default:
				break;
		}
	});
})();
