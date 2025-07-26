document.addEventListener('DOMContentLoaded', () => {
	
	//공고등록 버튼 페이지 이동
	document.querySelector('#createPostingBtn').addEventListener('click', function() {
 		window.location.href = '/corp/jobPostingForm'; // 페이지 이동
	});
	
	//헤더에 있는 값들, SecurityContext에서 가져와서 toString형태임  
	// JSON.parse(...) -> 문자열로 되어 있는 JSON 데이터를 JavaScript 객체로 변환해주는 함수 / 반대로 자바객체를 JSON으로 -> JSON.stringify()
	let userEmail = document.querySelector('#userEmail').value;
	let userRole = document.querySelector('#userRole').value;
	let userCorpNo = document.querySelector('#userCorpNo').value;
	let userInfo = document.querySelector('#userInfo').dataset.user;

	//데이터 비동기 가져오기 (매개변수 넣는거 까먹지마) 
	//fetchStartData(userCorpNo);

	//진행중, 마감, 전체버튼을 눌렀을 때 클래스 제거 및 추가
	changeClassSelectedTotd(userCorpNo);
	
	//전체, 마감일순, 조회수순 버튼을 눌렀을 때 클래스 제거 및 추가
	changeClassSubSelectedToInput(userCorpNo);
	
	//지원자 통계 모달창 열기 함수
	document.addEventListener('click', function(e){
		if(e.target.classList.contains("appl-stats-btn")){
			const jobPostingSeq = e.target.dataset.appliStats;
			showApplicantStatsModal(jobPostingSeq);

		}
	});
	
});//ready


//지원자 관리 모달창
function openApplicantListModal(){
	alert('지원자 관리 모달창 열림 대신에 페이지 이동으로 처리, 추후 모달창으로 간략 조회 기능구현할 것 ');
	location.href="/corp/applicant";
}

//처음화면 자료 뿌리기(진행중, 전체)
function fetchStartData(userCorpNo){
	var url =`/corp/getMyAllPosting?corpNo=${userCorpNo}&postSts=ing&orderBy=start`;
	fetch( url )
	.then(response => {
		if(!response){
			alert("데이터 갖고오기 실패");
			throw new Error("데이터를 가져오던 중에 서버 오류 발생");
		}
		return response.json();
	})
	.then(data => {
		//데이터가 존재할 떄
		if(data.length > 0){ 
			let resultData = document.querySelector('#resultData');
			
			console.log(data); //0-----------------------------------  처음 화면 자료조회 데이터는 controller에 model에 담아 쏘는 중 -------------------
			
			
		} else {
			//검색 결과가 없을 때
			
		}
	})
}

//진행중, 마감, 전체 클릭에 따라 클래스 추가 그리고 비동기로 조회 목록 가져오기 
function changeClassSelectedTotd(userCorpNo){
	//console.log(typeof userCorpNo); //String 이라서 number로 바꿔줘야 
	let corpNo = Number(userCorpNo); 
	let orderBy = "start"; //정률순은 고정 
	let postSts = "";
	document.querySelectorAll('.posting-status').forEach(td => {
		//초기값 동기적으로 부여 
		/* if(td.classList.contains('selected')){
		}; */
		td.addEventListener('click', () => {
			//기존에 선택된 td에서 클래스 제거 
			document.querySelectorAll('.posting-status').forEach(item => {
				item.classList.remove('selected');
			});
			//그러고 나서 클릭된 td에만 클래스 추가할거 
			td.classList.add('selected');
			
			//비동기로 해당 버튼에 맞는 데이터 리스트 가져오기(진행중ing, 마감end, 전체total) & 전체(공고최신순)
			if(td.id == 'getIngPosting'){
				postSts = "ing";
			} else if (td.id == 'getEndPosting'){
				postSts = "end";
			} else if (td.id == 'getAllPosting'){
				postSts = "total";
			}
			//자식버튼 클래스를 '전체'쪽으로 초기화 해줘 
			document.querySelectorAll('.posting-subStatus').forEach(item => {
				item.classList.remove('subSelected');
			});
			document.querySelector('.posting-subStatus').classList.add('subSelected'); //맨 처음요소(전체)에 클래스 부여 
			
			//alert('종류 : ' + postSts + ', 정렬순서 : ' + orderBy);
			
			let url = `/corp/postings/selectby/${corpNo}/${postSts}/${orderBy}`;
			fetch(url, {
				method : 'GET',
			})
			.then(response => {
				if(!response){
					throw new Error('조회 요청 실패');
				}
				return response.json(); //밑에서DOM다시 해야하니까 JSON으로, (.text() xxx)
			})
			.then(data => {
				console.log(data);
                updatePostingTable(data.postList);
                updatePostingCounts(data);
			})
			.catch(error => {
				
				console.error(error);
			})
		});
	});
}

//진행중, 마감, 전체 클릭에 따라 클래스 추가 그리고 비동기로 조회 목록 가져오기 
function changeClassSubSelectedToInput(userCorpNo){
	let corpNo = userCorpNo;
	let postSts = "ing";
	let orderBy = "start"; //초기값 
	
	document.querySelectorAll('.posting-subStatus').forEach(sub => {
		
		sub.addEventListener('click', () => {
			document.querySelectorAll('.posting-subStatus').forEach(item => {
				item.classList.remove('subSelected');
			});
			sub.classList.add('subSelected');
			
			//상위 버튼클래스 확인 먼저 
			document.querySelectorAll('.posting-status').forEach(item => {
				if(item.classList.contains('selected')){
					if(item.id == 'getIngPosting'){
						postSts = "ing";
					} else if(item.id == 'getEndPosting'){
						postSts = "end";
					} else if(item.id == 'getAllPosting'){
						postSts = "total";
					}
				}
			});
			
			//하위버튼 값 확인
			if(sub.id == 'odbSd'){
				orderBy = "start";
			} else  if(sub.id == 'odbEd'){
				orderBy = "end";
			} else  if (sub.id == 'odbVc'){
				orderBy = "viewCnt";
			}
			
			//alert('종류 : ' + postSts + ', 정렬순서 : ' + orderBy);
			
			//DOM 재구성 fetch 전송 
			let url = `/corp/postings/selectby/${corpNo}/${postSts}/${orderBy}`;
			fetch(url, {
				method : 'GET',
			})
			.then(response => {
				if(!response){
					throw new Error('조회 요청 실패');
				}
				return response.json(); //밑에서DOM다시 해야하니까 JSON으로, (.text() xxx)
			})
			.then(data => {
				console.log(data);
                updatePostingTable(data.postList);
                updatePostingCounts(data);
			})
			.catch(error => {
				
				console.error(error);
			})
			
		});
	});
}

//상단에 공고 n개씩 보기 설정
function selectPostingByCnt(){
	alert('미구현');
}


//수정 버튼 클릭
function updatePosting(jobPostingSeq){
	if(confirm('jobPostingSeq : ' + jobPostingSeq + ', 공고 수정 ?')){
		location.href=`/corp/postings/updateForm/${jobPostingSeq}`;
	}
}

//마감 버튼 클릭 (한번 RESTful API 호출방법으로 해볼까?)
function finishPosting(jobPostingSeq){
	if(confirm('해당 공고를 조기 마감하시겠습니까?')){
		let url = `/corp/postings/${jobPostingSeq}`;
		fetch(url, {
			method: 'DELETE',
		})
		.then(response => {
			if(!response.ok){
				throw new Error('요청 실패');
			} else {
				return response.text();
			}
		})
		.then(result => {
			alert(result);
			location.href="/corp/myJobPostingListPage";
		})
		.catch(error => {
			alert('공고 조기마감 처리 실패');
			console.error(error);
		})
	}
	return false;
}

//공고 상세보기 (유저페이지로 이동)
function getDetailPostingInfo(jobPostingSeq){
	location.href=`/user/job_posting/job_posting_detail?jobPostingSeq=${jobPostingSeq}`;
}

//DOM 비동기 처리(전체, 진행중, 마감)
function updatePostingTable(postList) {
    const tbody = document.querySelector('#resultData');
    tbody.innerHTML = ''; // 초기화 
    
    if (!postList || postList.length === 0) {
        const trEmpty = document.createElement('tr');
        trEmpty.innerHTML = `<td colspan='5' style="text-align: center;">조회 목록이 존재하지 않습니다.</td>`;
        tbody.appendChild(trEmpty);
    } else {
	    postList.forEach(post => {
	        const tr = document.createElement('tr');
	        tr.innerHTML = `
	            <td class="posting-box-need-center">${post.isEnded == 'N' ? '진행 중' : '공고 마감'}</td>
	            <td>
	                <a><span id="jobPosting-${post.jobPostingSeq}" class="detail-posting-link" onclick="getDetailPostingInfo(${post.jobPostingSeq})">${post.postingTitle}</span></a>
	                <ul>
	                    <li>근무형태 : <span>${post.employType}</span></li>
	                    <li>포지션 : <span>${post.positionName}</span></li>
	                    <li>경력 : <span>${post.expLevel}</span></li>
	                    <li>접수기간 : <span>${post.postingStartDt}</span> ~ <span>${post.postingEndDt}</span></li>
	                    <li>조회수 : <span>${post.viewCnt} </span></li>
	                </ul>
	            </td>`;
	            let nOrY = '';
	            if(post.isEnded == 'N'){
	            	nOrY = `
		            <td class="posting-box-need-center">
		                <button type="button" onclick="updatePosting(${post.jobPostingSeq})">수정</button>
		                <button type="button" onclick="finishPosting(${post.jobPostingSeq})">마감</button>
		            </td>`;
	            }
	            if(post.isEnded == 'Y'){
	            	nOrY =`
					<td class="posting-box-need-center">
						<button type="button" style='background-color:grey;' disabled>수정 </button>
						<button type="button" style='background-color:grey;' disabled>마감 </button>
					</td>`;
	            }
	            tr.innerHTML += nOrY;
	            tr.innerHTML += `
	            <td class="posting-box-need-center">
	                <div><a style="cursor: pointer; text-decoration: underline;" onclick="openApplicantListModal()"><span>${post.appCnt}</span></a></div>
	                <div style="color: black; cursor: pointer;" onclick="openApplicantListModal()">지원자 관리 <i class="bi bi-file-earmark-ruled"></i></div>
	                <div><input type="button" value="지원자 통계" onclick="getApplicantStats(${post.jobPostingSeq})"></div>
	            </td>
	            <td class="posting-box-need-center">
	                <button type="button">확인</button>
	            </td>
	        	`;
	        tbody.appendChild(tr);
	    });
   	}
}
 
//건수 업데이트
function updatePostingCounts(data) {
    if (data.postCnt) {
        const counts = data.postCnt[0];

        const ingTh = document.querySelector('#getIngPosting');
        if (ingTh) ingTh.innerHTML = `진행중 (<span>${counts.ING_POSTING_CNT || 0}</span>)건`;

        const endTh = document.querySelector('#getEndPosting');
        if (endTh) endTh.innerHTML = `마감 (<span>${counts.END_POSTING_CNT || 0}</span>)건`;

        const totalTh = document.querySelector('#getAllPosting');
        if (totalTh) totalTh.innerHTML = `전체 (<span>${counts.TOTAL_POSTING_CNT || 0}</span>)건`;
    }
}

//지원자 통계 모달창 보기
function showApplicantStatsModal(jobPostingSeq){
	
	//모달열기
	const modal = new bootstrap.Modal(document.getElementById('applicant-stats'));
	modal.show();
	
	//모달 열린 후 콜백 실행 
	document.querySelector('.modal-title').textContent = '제목임시입니다.';

	
}

