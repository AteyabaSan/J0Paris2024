package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joparis2024.dto.RoleDTO;
import com.joparis2024.mapper.RoleMapper;
import com.joparis2024.model.Role;
import com.joparis2024.repository.RoleRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    private RoleDTO roleDTO;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDTO = new RoleDTO();
        roleDTO.setName("ADMIN");

        role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
    }

    @Test
    void testCreateRole_Success() throws Exception {
        when(roleRepository.findByName(roleDTO.getName())).thenReturn(Optional.empty());
        when(roleMapper.toEntity(roleDTO)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toDTO(role)).thenReturn(roleDTO);

        RoleDTO createdRole = roleService.createRole(roleDTO);

        assertNotNull(createdRole);
        assertEquals("ADMIN", createdRole.getName());
        verify(roleRepository, times(1)).findByName(roleDTO.getName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testCreateRole_RoleAlreadyExists() {
        when(roleRepository.findByName(roleDTO.getName())).thenReturn(Optional.of(role));

        Exception exception = assertThrows(Exception.class, () -> {
            roleService.createRole(roleDTO);
        });

        assertEquals("Le rôle existe déjà", exception.getMessage());
        verify(roleRepository, times(1)).findByName(roleDTO.getName());
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void testUpdateRole_Success() throws Exception {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toDTO(role)).thenReturn(roleDTO);

        RoleDTO updatedRole = roleService.updateRole(1L, roleDTO);

        assertNotNull(updatedRole);
        assertEquals("ADMIN", updatedRole.getName());
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testUpdateRole_RoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            roleService.updateRole(1L, roleDTO);
        });

        assertEquals("Rôle non trouvé", exception.getMessage());
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void testGetAllRoles() {
        List<Role> roles = Arrays.asList(role);
        when(roleRepository.findAll()).thenReturn(roles);
        when(roleMapper.toDTO(role)).thenReturn(roleDTO);

        List<RoleDTO> roleDTOs = roleService.getAllRoles();

        assertNotNull(roleDTOs);
        assertEquals(1, roleDTOs.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testGetRoleById_Success() throws Exception {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleMapper.toDTO(role)).thenReturn(roleDTO);

        RoleDTO foundRole = roleService.getRoleById(1L);

        assertNotNull(foundRole);
        assertEquals("ADMIN", foundRole.getName());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRoleById_RoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            roleService.getRoleById(1L);
        });

        assertEquals("Rôle non trouvé", exception.getMessage());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteRole_Success() throws Exception {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        roleService.deleteRole(1L);

        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).delete(role);
    }

    @Test
    void testDeleteRole_RoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            roleService.deleteRole(1L);
        });

        assertEquals("Rôle non trouvé", exception.getMessage());
        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, never()).delete(any(Role.class));
    }

    @Test
    void testFindByName_Success() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
        when(roleMapper.toDTO(role)).thenReturn(roleDTO);

        RoleDTO foundRole = roleService.findByName("ADMIN");

        assertNotNull(foundRole);
        assertEquals("ADMIN", foundRole.getName());
        verify(roleRepository, times(1)).findByName("ADMIN");
    }

    @Test
    void testFindByName_RoleNotFound() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roleService.findByName("ADMIN");
        });

        assertEquals("Role not found", exception.getMessage());
        verify(roleRepository, times(1)).findByName("ADMIN");
    }
}
