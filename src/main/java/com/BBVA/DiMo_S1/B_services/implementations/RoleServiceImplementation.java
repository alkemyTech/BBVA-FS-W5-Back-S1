package com.BBVA.DiMo_S1.B_services.implementations;

import com.BBVA.DiMo_S1.B_services.interfaces.RoleService;
import com.BBVA.DiMo_S1.C_repositories.RoleRepository;
import com.BBVA.DiMo_S1.D_models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImplementation implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(final String name) {

        return roleRepository.findByName(name);
    }
    @Override
    public Role findById(Long id){
        return roleRepository.findById(id).orElse(null);
    }
}
