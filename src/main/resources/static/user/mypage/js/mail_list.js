document.addEventListener('DOMContentLoaded', function() {

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
});

//메일 통계 업데이트 함수
function updateCounts() {
	const total = document.querySelectorAll('.application-item').length;
	const read = document.querySelectorAll('.application-item.read-mail').length;
	const unread = total - read;

	document.getElementById('total-count').textContent = total;
	document.getElementById('read-count').textContent = read;
	document.getElementById('unread-count').textContent = unread;
}
