document.addEventListener('DOMContentLoaded', function() {

	const pagination = document.querySelector('.pagination-container');

	//페이지 버튼 클릭시
	document.querySelectorAll('.page-btn[data-page]').forEach(button => {
		button.addEventListener('click', function() {
			const clickPage = this.getAttribute('data-page');
			movePage(clickPage);
		});
	});

	//이전 버튼 클릭시
	document.querySelector('.prev').addEventListener('click', function() {
		const startPage = parseInt(pagination.dataset.startPage);
		const pageGroup = parseInt(pagination.dataset.pageGroup);
		const prevPage = startPage - pageGroup;
		if (prevPage >= 1) {
			movePage(prevPage)
		}
	});

	//이후 버튼 클릭시
	document.querySelector('.next').addEventListener('click', function() {
		const endPage = parseInt(pagination.dataset.endPage);
		const totalPage = parseInt(pagination.dataset.totalPage);
		const nextPage = endPage + 1;
		if (nextPage <= totalPage) {
			movePage(nextPage)
		}
	});
	
	//각 필터 버튼 클릭시
	document.querySelectorAll('.stats-item').forEach(item => {
		item.addEventListener('click', function(){
			const type = item.dataset.filter;
			movePage(1, type);
		});
	});
	
	//검색 버튼 및 enter 버튼 클릭시
	document.getElementById('search').addEventListener('click', function(){
		movePage(1);
	});

	//메일 클릭시 메일 상세내용 뜨기
	document.querySelectorAll('.application-item').forEach(function(item) {
		item.addEventListener('click', async function() {
			const title = item.querySelector('.job-title')?.textContent || '제목 없음';
			const company = item.querySelector('.company-name')?.textContent || '회사명';
			const date = item.querySelector('.application-meta')?.textContent || '날짜 정보 없음';
			const content = item.getAttribute('data-content') || '내용 없음';

			document.getElementById('modalTitle').textContent = title;
			document.getElementById('modalCompany').textContent = company;
			document.getElementById('modalDate').textContent = date;
			document.getElementById('modalMessage').innerHTML = content;

			document.getElementById('mailModal').style.display = 'flex';


			//이미 읽은 메일이면 읽음 처리 필요없으니 return
			if (item.classList.contains('read-mail')) return;

			//처음 클릭한 메일이면 메일 읽음 처리
			const messageSeq = item.getAttribute('data-messageSeq');

			try {
				const response = await fetch('/mypage/message/' + messageSeq, {
					method: "PUT"
				});

				if (response.ok) {
					item.classList.add('read-mail');
					updateCounts();
				} else {
					alert("서버 응답 오류")
				}

			} catch (error) {
				console.error("에러 발생:", error);
			}
		});
	});

	//모달 닫기
	document.querySelector('.modal-close').addEventListener('click', function() {
		document.getElementById('mailModal').style.display = 'none';
	});

	//모달 밖 클릭시 닫기
	document.querySelector('.modal-overlay').addEventListener('click', function() {
		document.getElementById('mailModal').style.display = 'none';
	});

	// 체크박스 클릭시 모달 안 뜨도록 막기
	document.querySelectorAll('.application-item input[type="checkbox"]').forEach(checkbox => {
		checkbox.addEventListener('click', function(event) {
			event.stopPropagation();
		});
	});

	// 전체 체크박스 클릭
	document.getElementById('selectAllCheckbox').addEventListener('change', function() {
		const checked = this.checked;
		document.querySelectorAll('.mail-item-checkbox').forEach(chk => {
			chk.checked = checked;
		});
	});

});

function movePage(currentPage, type) {
	const keyword = document.querySelector('input[name="keyword"]').value
	const params = new URLSearchParams(location.search);
	params.set("page", currentPage);
	params.set("keyword", keyword);

	// type을 새로 전달했다면 반영, 아니면 기존 유지
	if (type !== undefined && type !== "") {
		params.set("type", type);
	}

	location.href = "/user/mypage/mail_list?" + params.toString();
}

//메일 통계 업데이트 함수
/*function updateCounts() {
	const total = document.querySelectorAll('.application-item').length;
	const read = document.querySelectorAll('.application-item.read-mail').length;
	const unread = total - read;

	document.getElementById('total-count').textContent = total;
	document.getElementById('read-count').textContent = read;
	document.getElementById('unread-count').textContent = unread;
}*/


//ajax로 메일 목록 갱신하기
/*async function movePage(currentPage) {
	const params = new URLSearchParams({
		page: currentPage,
	});

	const response = await fetch('/mypage/messages?' + params.toString(), {
		method: 'GET',
		headers: { 'Content-Type': 'application/json' },
	});

	if (!response.ok) {
		throw new Error("메일을 불러오는데 실패했습니다.")
	}

	const result = await response.json();
	if (result.result == 'success') {
		const listContainer = document.querySelector('.application-list');
		listContainer.innerHTML = '';

		result.data.forEach(message => {
			const div = document.createElement('div');
			div.className = 'application-item';
			if (message.isRead === 'Y') {
				div.classList.add('read-mail');
			}
			div.dataset.messageSeq = message.messageSeq;
			div.dataset.content = message.messageContent;

			div.innerHTML = `
				<div calss="application-info">
				<div class="company-name">${message.corpNm}</div>
				<div class="job-title">${message.messageTitle}</div>
				<div class="application-meta">${message.createdAt}</div>
				</div>
			`;
			
			listContainer.appendChild(div);
		});
	}

}*/
