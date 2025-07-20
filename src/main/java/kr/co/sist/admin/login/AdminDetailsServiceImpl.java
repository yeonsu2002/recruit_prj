package kr.co.sist.admin.login;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.sist.admin.AdminDTO;
import kr.co.sist.admin.AdminEntity;
import kr.co.sist.admin.AdminRepository;
import kr.co.sist.jwt.CustomAdmin;
import lombok.RequiredArgsConstructor;

@Service
public class AdminDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminDetailsServiceImpl(AdminRepository adminRepository) {
    	this.adminRepository = adminRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String adminId) throws UsernameNotFoundException {
    		Thread.dumpStack();
        System.out.println("👉 loadUserByUsername() called with ID: " + adminId);
        
        AdminEntity admin = adminRepository.findById(adminId)
            .orElseThrow(() -> new UsernameNotFoundException("등록되지 않은 관리자 ID입니다."));

        System.out.println("✅ adminRepository.findById() success: " + admin.getAdminId());
        
        CustomAdmin customAdmin = new CustomAdmin(admin);
        System.out.println("✅ CustomAdmin created");

        return customAdmin;
    }

}
