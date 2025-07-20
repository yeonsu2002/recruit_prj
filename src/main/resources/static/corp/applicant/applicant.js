document.addEventListener('DOMContentLoaded', function() {

	const postingStatus = document.querySelector('select[name="postingStatus"]');

	//페이지 진입시 실행
	updatePostingTitle(postingStatus.value);

	//옵션 선택시 함수 실행
	postingStatus.addEventListener('change', function() {
		updatePostingTitle(this.value);
	});

	//검색 조건 넘기기
	document.querySelector('.search-btn').addEventListener('click', function() {
		const params = {
			//size: document.querySelector('select[name="size"]').value,
			postingStatus: document.querySelector('select[name="postingStatus"]').value,
			postingTitle: document.querySelector('select[name="postingTitle"]').value,
			applicationStatus: document.querySelector('select[name="applicationStatus"]').value,
			passStage: document.querySelector('select[name="passStage"]').value,
			keyword: document.querySelector('input[name="keyword"]').value
		}

		fetch('/corp/applicant/search', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(params)
		})
			.then(response => {
				if (!response.ok) throw new Error('서버 오류 : ' + response.status)
				return response.json();
			})
			.then(result => {
				const applicants = result.applicants;
				const tableContainer = document.querySelector('.table-container');
				const oldRows = tableContainer.querySelectorAll('.table-row');
				oldRows.forEach(row => row.remove());

				applicants.forEach(applicant => {
					const row = document.createElement('div');
					row.classList.add('table-row');

					row.innerHTML = `
					<div class="col-name">${applicant.name}</div>
					<div class="col-period">${applicant.careerType}</div>
					<div class="col-position">${applicant.postingTitle}</div>
					<div class="col-company">${applicant.statusName}</div>
					<div class="col-resume" data-resume="${applicant.resumeSeq}">${applicant.title}</div>
					<div class="col-date">${applicant.applicationDate}</div>
					<div class="col-status">
						<span class="status-badge${getStatusClass(applicant.stageName)}">${applicant.stageName}</span>
					</div>					
				`;
				
				tableContainer.appendChild(row);
				});

			})
			.catch(error => {
				alert("요청 실패 : " + error.message)
			})
	});
	
	//이력서 클릭시 지원자 상세보기로 이동
	document.querySelectorAll('.col-resume').forEach( e => {
		e.addEventListener('click', function(){
			const resumeSeq = this.getAttribute('data-resume');
			const jobPostingSeq = this.getAttribute('data-posting');
			location.href = `/corp/applicant/resume/${resumeSeq}/${jobPostingSeq}`;			
		});
	});
	

});

//전체, 진행중, 마감 공고 선택에 따른 공고 종류 변경
function updatePostingTitle(status) {

	const postingTitle = document.querySelector('select[name="postingTitle"]');

	fetch('/corp/applicant?status=' + status, {
		method: 'POST'
	})
		.then(response => {
			if (!response.ok) throw new Error('서버 오류 : ' + response.status)

			return response.json();
		})
		.then(data => {
			postingTitle.innerHTML = '';

			// 기본 옵션 추가
			const defaultOption = document.createElement('option');
			defaultOption.textContent = data.defaultOption;
			defaultOption.value = "0";
			postingTitle.appendChild(defaultOption);

			// 받아온 공고 리스트 추가
			data.postings.forEach(posting => {
				const opt = document.createElement('option');
				opt.textContent = posting.postingTitle;
				opt.value = posting.jobPostingSeq;
				postingTitle.appendChild(opt);
			});

		})
		.catch(error => {
			alert("요청 실패 : " + error.message)
		})
}

function getStatusClass(stageName) {
	switch (stageName) {
		case '신규지원': return ' status-applying';
		case '서류통과': return ' status-reviewing';
		case '최종합격': return ' status-pending';
		case '불합격': return ' status-rejected';
		default: return '';
	}
}