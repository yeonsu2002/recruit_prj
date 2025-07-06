$(function() {
	/** 사진 업로드 초기화 */
	const resetPhotoUploadBox = () => {
		$('#photo-upload-box')
			.html(`
        <div class="photo-upload-text">
          <i class="fas fa-camera"></i>
          <div>사진 업로드</div>
          <small>클릭하여 선택</small>
        </div>
        <button type="button" class="photo-remove-btn" id="photo-remove-btn">×</button>
      `)
			.removeClass('has-photo');
		$('#photo-input').val('');
	};

	// 사진 업로드 클릭 시 파일 선택창 열기
	$('#photo-upload-box').on('click', function(e) {
		// 클릭된 요소가 업로드 박스 자체 또는 자식 중 삭제버튼이 아닌 경우에만 파일 선택창 열기
		if (!$(e.target).is('#photo-remove-btn')) {
			$('#photo-input').click();
		}
	});

	// 사진 업로드 후 미리보기 표시
	$('#photo-input').on('change', function() {
		const file = this.files[0];
		if (!file) return;

		const reader = new FileReader();
		reader.onload = e => {
			$('#photo-upload-box')
				.html(`<img src="${e.target.result}" alt="프로필 사진">
               <button type="button" class="photo-remove-btn" id="photo-remove-btn">×</button>`)
				.addClass('has-photo');
		};
		reader.readAsDataURL(file);
	});

	// 사진 삭제 (이벤트 위임)
	$(document).on('click', '#photo-remove-btn', e => {
		e.stopPropagation();
		resetPhotoUploadBox();
	});


	//직무 저장해놓는 배열
	let positionSelected = [];

	//직무 클릭시 리스트 열기
	$("#positionSelect").on("click", function() {
		$("#optionsList").toggle();
	});

	//직무 클릭시
	$('#optionsList li').on('click', function() {
		let position = $(this).data("value");
		let text = $(this).text();

		// positions 영역에 이미 같은 텍스트를 가진 태그가 있으면 삭제
		$('#positions .tag').each(function() {
			if ($(this).text().replace('×', '').trim() === text) {
				$(this).remove();
			}
		});

		$('#positions').append(
			`<span class="tag">${text}
             <button type="button" class="remove-tag">×</button>
          	 </span>`
		);
	});


	//외부 클릭시 직무 리스트 닫기
	$(document).on("click", function(e) {
		if (!$(e.target).closest("#positionSelect").length) {
			$("#optionsList").hide();
		}
	});

	// 기술스택 입력 처리
	$("#skill-input").on("keyup", function() {
		let keyword = $(this).val();

		if (keyword === "") {
			// 입력이 비어있으면 목록 숨기고 종료
			$('#skillsList').empty();
			$("#skillsList").hide();
			return;
		}

		$.ajax({
			url: "/techStack/search",
			type: 'GET',
			data: { keyword: keyword },
			success: function(response) {
				$('#skillsList').empty();
				response.forEach(item => {
					$('#skillsList').append(`<li data-value="${item.techStackSeq}">${item.stackName}</li>`);
					if (response.length > 0) {
						$("#skillsList").show();
					} else {
						$("#skillsList").hide();
					}
				});
			},
			error: function(xhr, status, error) {
				console.error('오류:', error);
				alert('검색 중 오류 발생');
			}
		});

	});

	$('#skillsList').on('click', 'li', function() {
		let position = $(this).data("value");
		let text = $(this).text();

		$('#skills').append(
			`<span class="tag">${text}
	            <button type="button" class="remove-tag">×</button>
	         </span>`
		);
	});

	//외부 클릭시 기술 스택 리스트 닫기
	$(document).on("click", function(e) {
		if (!$(e.target).closest("#positionSelect").length) {
			$("#skillsList").hide();
		}
	});


	// 기술&포지션 태그 삭제 (이벤트 위임)
	$(document).on('click', '.remove-tag', function() {
		$(this).parent().remove();
	});


	/** 학력/경력/프로젝트/기타 공통 추가 함수 */
	const addItem = (containerSelector, html) => $(containerSelector).append(html);

	// 각 추가 버튼에 맞게 아이템 추가
	$('#add-edu').on('click', () => addItem('.edu-list', eduHtml()));
	$('#add-exp').on('click', () => addItem('.exp-list', expHtml()));
	$('#add-proj').on('click', () => addItem('.proj-list', projHtml()));
	$('#add-etc').on('click', () => addItem('.etc-list', etcHtml()));
	$('#add-intro').on('click', () => addItem('.intro-list', introHtml()));

	// 각 항목별 HTML 템플릿 함수 (필요한 항목 추가 시 유지보수 용이)
	const eduHtml = () => `
	<div class="item-box">
	  <button type="button" class="item-remove-btn">×</button>
	  <div class="row g-3">
	    <div class="col-md-2">
	      <label class="form-label">입학 날짜</label>
	      <input type="text" class="form-control" placeholder="YYYY.MM">
	    </div>
	    <div class="col-md-2">
	      <label class="form-label">졸업날짜(예정)</label>
	      <input type="text" class="form-control" placeholder="YYYY.MM">
	    </div>
	    <div class="col-md-4">
	      <label class="form-label">학교명</label>
	      <input type="text" class="form-control" placeholder="○○대학교">
	    </div>
	    <div class="col-md-4">
	      <label class="form-label">학과</label>
	      <input type="text" class="form-control" placeholder="컴퓨터공학과">
	    </div>
	    <div class="col-md-2">
	      <label class="form-label">학력구분</label>
	      <select class="form-select">
	        <option selected disabled>선택해주세요</option><option>고등학교</option><option>대학(2,3년)</option><option>대학(4년)</option><option>대학원(석사)</option><option>대학원(박사)</option>
	      </select>
	    </div>
	    <div class="col-md-2">
	      <label class="form-label">학점</label>
	      <input type="text" class="form-control" placeholder="예: 3.5">
	    </div>
	    <div class="col-md-2">
	      <label class="form-label">기준학점</label>
	      <select class="form-select">
	        <option selected disabled>선택해주세요</option><option>4.5</option><option>4.3</option><option>4.0</option><option>5.0</option><option>7.0</option><option>100</option>
	      </select>
	    </div>
	  </div>
	</div>
	`;


	const expHtml = () => `
    <div class="item-box">
      <button type="button" class="item-remove-btn">×</button>
      <div class="row g-3">
        <div class="col-md-2"><label class="form-label">입사년월</label><input type="text" class="form-control" placeholder="YYYY.MM"></div>
        <div class="col-md-2"><label class="form-label">퇴사년월</label><input type="text" class="form-control" placeholder="YYYY.MM"></div>
        <div class="col-md-4"><label class="form-label">회사명</label><input type="text" class="form-control" placeholder="(주)○○회사"></div>
        <div class="col-md-4"><label class="form-label">직책/직무</label><input type="text" class="form-control" placeholder="주임/백엔드 개발자"></div>
        <div class="col-12"><label class="form-label">담당업무 및 성과</label><textarea class="form-control" rows="3" placeholder="담당했던 업무와 성과를 구체적으로 입력해주세요"></textarea></div>
      </div>
    </div>`;

	const projHtml = () => `
    <div class="item-box">
      <button type="button" class="item-remove-btn">×</button>
      <div class="row g-3">
        <div class="col-md-6"><label class="form-label">프로젝트 기간</label><input type="text" class="form-control" placeholder="2023.05 ~ 2023.08"></div>
        <div class="col-md-6"><label class="form-label">프로젝트명</label><input type="text" class="form-control" placeholder="쇼핑몰 웹사이트 개발"></div>
        <div class="col-12"><label class="form-label">사용 기술</label><input type="text" class="form-control" placeholder="React, Node.js, MongoDB, AWS"></div>
        <div class="col-12"><label class="form-label">상세 내용</label><textarea class="form-control" rows="4" placeholder="프로젝트에서 담당한 역할과 구현한 기능을 상세히 설명해주세요"></textarea></div>
        <div class="col-md-6"><div class="form-check mt-3"><input class="form-check-input" type="checkbox"><label class="form-check-label">배포/출시 완료</label></div></div>
        <div class="col-md-6"><label class="form-label">저장소 링크</label><input type="url" class="form-control" placeholder="https://github.com/username/project"></div>
      </div>
    </div>`;

	const etcHtml = () => `
    <div class="item-box">
      <button type="button" class="item-remove-btn">×</button>
      <div class="row g-3">
        <div class="col-md-2"><label class="form-label">취득일/종료일</label><input type="text" class="form-control" placeholder="YYYY.MM"></div>
        <div class="col-md-4"><label class="form-label">활동명/자격증명</label><input type="text" class="form-control" placeholder="정보처리기사"></div>
        <div class="col-md-2"><label class="form-label">이력 구분</label><select class="form-select"><option selected disabled>선택해주세요</option><option>자격증</option><option>수상이럭</option><option>대외활동</option><option>어학</option></select></div>
        <div class="col-md-4"><label class="form-label">관련 기관</label><input type="text" class="form-control" placeholder="한국산업인력공단"></div>
        <div class="col-12"><label class="form-label">상세 내용</label><textarea class="form-control" rows="3" placeholder="자세한 내용이나 성과를 입력해주세요"></textarea></div>
      </div>
    </div>`;

	const introHtml = () => `
	<div class="item-box">
	<button type="button" class="item-remove-btn">×</button>
	<div class="mb-3">
	<label class="form-label">제목</label> <input type="text"class="form-control" placeholder="자기소개서 제목을 입력해주세요">
	</div>
	<div class="mb-3">
	<label class="form-label">내용</label>
	<textarea class="form-control" rows="8" placeholder="자기소개 내용을 입력해주세요"></textarea>
	</div>
	</div>
	`

	// 공통 삭제 버튼 처리 (이벤트 위임)
	$(document).on('click', '.item-remove-btn', function() {
		$(this).parent().remove();
	});

	document.addEventListener('input', function(e) {
		if (e.target.placeholder === 'YYYY.MM') {
			let value = e.target.value.replace(/\D/g, '');
			if (value.length >= 4) {
				value = value.substring(0, 4) + '.' + value.substring(4, 6);
			}
			e.target.value = value;
		}
	});
});