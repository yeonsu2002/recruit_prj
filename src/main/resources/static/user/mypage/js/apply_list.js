document.addEventListener('DOMContentLoaded', function() {

	//지원한 이력서 클릭시
	document.querySelectorAll('.view-resume').forEach(function(element) {
		element.addEventListener('click', function(e) {
			const resumeSeq = e.target.getAttribute("data-resumeSeq")
			location.href = "/user/resume/resume_form/" + resumeSeq;
		});
	});

	//지원 취소 클릭시
	document.querySelectorAll('.cancel-applicant').forEach(function(element) {
		element.addEventListener('click', async function(e) {
			const jobApplicationSeq = e.target.getAttribute("data-applicantSeq");

			if (!confirm("정말로 지원을 취소하시겠습니까?")) return;

			try {
				const response = await fetch('/mypage/application/' + jobApplicationSeq, {
					method: "DELETE"
				});
				
				const result = await response.text();
				
				if (response.ok) {
					if(result == "success"){
						alert("지원 취소 완료")
						const applicationItem = e.target.closest(".application-item")
						applicationItem.classList.add('read-mail');
						
						const badge = applicationItem.querySelector(".badge");
						badge.classList.add('status-canceled');
						badge.textContent = "지원취소";
					} else{
						alert("지원 취소 실패")
					}
				} else{
					alert("서버 응답 오류")
				}
			} catch (error) {
				console.error("에러 발생:", error);
			}
		});
	});
});
