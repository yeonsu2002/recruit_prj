document.addEventListener('DOMContentLoaded', function() {

	//공고 클릭시 공고 상세 페이지로 이동
	document.querySelectorAll('.announcement-card').forEach(function(element){
		element.addEventListener('click', function(){
			const postingSeq = element.dataset.postingSeq;
			location.href = "/user/job_posting/job_posting_detail?jobPostingSeq=" + postingSeq;
		});
	});
});