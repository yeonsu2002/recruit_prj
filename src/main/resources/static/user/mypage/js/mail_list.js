document.addEventListener('DOMContentLoaded', function(){
	
	//메일 클릭시 메일 상세내용 뜨기
	document.querySelectorAll('.application-item').forEach(function(item) {
	    item.addEventListener('click', function() {
	        const title = item.querySelector('.job-title')?.textContent || '제목 없음';
	        const company = item.querySelector('.company-name')?.textContent || '회사명';
	        const date = item.querySelector('.application-meta')?.textContent || '날짜 정보 없음';
	        const content = item.getAttribute('data-content') || '내용 없음'; 

	        document.getElementById('modalTitle').textContent = title;
	        document.getElementById('modalCompany').textContent = company;
	        document.getElementById('modalDate').textContent = date;
	        document.getElementById('modalMessage').innerHTML = content; 

	        document.getElementById('mailModal').style.display = 'flex';
			
	    });
	});

	document.querySelector('.modal-close').addEventListener('click', function() {
	    document.getElementById('mailModal').style.display = 'none';
	});
	document.querySelector('.modal-overlay').addEventListener('click', function() {
	    document.getElementById('mailModal').style.display = 'none';
	});
});