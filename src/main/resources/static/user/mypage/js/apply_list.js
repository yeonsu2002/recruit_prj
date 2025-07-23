document.addEventListener('DOMContentLoaded', function() {

	//지원한 이력서 클릭시
	document.querySelectorAll('.view-resume').forEach(function(element) {
		element.addEventListener('click', function(e) {
			const resumeSeq = e.target.getAttribute("data-resumeSeq")
			location.href = "/user/resume/resume_form/" + resumeSeq;
		});
	});

	//각 필터 버튼 클릭시
	document.querySelectorAll('.stats-item').forEach(item => {
		item.addEventListener('click', function() {
			const type = item.dataset.filter;
			movePage(type, "");

		});
	});

	//체크박스 클릭시
	const checkbox = document.getElementById('exclude-canceled');

	checkbox.addEventListener('change', function() {
		if (checkbox.checked) {
			movePage("", "false")
		} else {
			movePage("", "true")
		}
	});

	//날짜 옵션 변경시
	document.getElementById('period').addEventListener('change', function() {
		movePage("", "")
	});

	//지원 취소 클릭시
	document.querySelectorAll('.cancel-applicant').forEach(function(element) {
		element.addEventListener('click', async function(e) {

			const jobApplicationSeq = e.target.getAttribute("data-applicantSeq");

			if (e.target.innerText == '지원취소') {

				if (!confirm("정말로 지원을 취소하시겠습니까?")) return;

				try {
					const response = await fetch('/mypage/application/' + jobApplicationSeq, {
						method: "PUT"
					});

					const result = await response.text();

					if (response.ok) {
						if (result == "success") {
							alert("지원 취소 완료")
							const applicationItem = e.target.closest(".application-item")
							applicationItem.classList.add('read-mail');
							
							const applicationMeta = applicationItem.querySelector(".cancel-applicant");
							applicationMeta.innerText = "삭제";

							const badge = applicationItem.querySelector(".badge");
							badge.classList.add('status-canceled');
							badge.textContent = "지원취소";
						} else {
							alert("지원 취소 실패")
						}
					} else {
						alert("서버 응답 오류")
					}
				} catch (error) {
					console.error("에러 발생:", error);
				}
				
				return;
			}

			if (e.target.innerText == '삭제') {

				if(!confirm("정말로 지원내역을 삭제하시겠습니까?")) return;
				
				try{
					const response = await fetch('/mypage/application/' + jobApplicationSeq, {
						method : 'DELETE'
					});
					
					if(response.ok){
						alert("삭제 완료")
						location.reload();						
					} else{
						alert("서버 응답 오류")
					}
				} catch(error){
					console.error("에러 발생:", error);
					alert("에러 발생");
				}
				
				return;
			}
		});
	})
	

});

//페이지 이동
function movePage(type, includeCanceled) {
	const params = new URLSearchParams(location.search);
	params.set("period", document.getElementById('period').value);

	if (type !== undefined && type !== "") {
		params.set("type", type);
	}

	if (includeCanceled != undefined && includeCanceled != "") {
		params.set("includeCanceled", includeCanceled);
	}

	location.href = "/user/mypage/apply_list?" + params.toString();
}

