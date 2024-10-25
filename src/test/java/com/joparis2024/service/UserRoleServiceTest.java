package com.joparis2024.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joparis2024.dto.UserRoleDTO;
import com.joparis2024.mapper.UserRoleMapper;
import com.joparis2024.model.Role;
import com.joparis2024.model.User;
import com.joparis2024.model.UserRole;
import com.joparis2024.repository.RoleRepository;
import com.joparis2024.repository.UserRepository;
import com.joparis2024.repository.UserRoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {

    @InjectMocks
    private UserRoleService userRoleService;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void testCreateUserRole_Success() throws Exception {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(1L);
        userRoleDTO.setRoleId(1L);

        User user = new User();
        Role role = new Role();
        UserRole userRole = new UserRole();

        when(userRepository.findById(userRoleDTO.getUserId())).thenReturn(Optional.of(user));
        when(roleRepository.findById(userRoleDTO.getRoleId())).thenReturn(Optional.of(role));
        when(userRoleMapper.toEntity(userRoleDTO)).thenReturn(userRole);
        when(userRoleRepository.save(userRole)).thenReturn(userRole);
        when(userRoleMapper.toDTO(userRole)).thenReturn(userRoleDTO);

        UserRoleDTO result = userRoleService.createUserRole(userRoleDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userRoleDTO.getUserId());
        verify(roleRepository, times(1)).findById(userRoleDTO.getRoleId());
        verify(userRoleRepository, times(1)).save(userRole);
        verify(userRoleMapper, times(1)).toDTO(userRole);
    }

    @Test
    void testCreateUserRole_UserNotFound() {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(1L);
        userRoleDTO.setRoleId(1L);

        when(userRepository.findById(userRoleDTO.getUserId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> userRoleService.createUserRole(userRoleDTO));
        assertEquals("Utilisateur non trouvé", exception.getMessage());
    }

    @Test
    void testCreateUserRole_RoleNotFound() {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(1L);
        userRoleDTO.setRoleId(1L);

        User user = new User();
        when(userRepository.findById(userRoleDTO.getUserId())).thenReturn(Optional.of(user));
        when(roleRepository.findById(userRoleDTO.getRoleId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> userRoleService.createUserRole(userRoleDTO));
        assertEquals("Rôle non trouvé", exception.getMessage());
    }

    @Test
    void testUpdateUserRole_Success() throws Exception {
        Long userRoleId = 1L;
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(1L);
        userRoleDTO.setRoleId(1L);

        UserRole userRole = new UserRole();
        User user = new User();
        Role role = new Role();

        when(userRoleRepository.findById(userRoleId)).thenReturn(Optional.of(userRole));
        when(userRepository.findById(userRoleDTO.getUserId())).thenReturn(Optional.of(user));
        when(roleRepository.findById(userRoleDTO.getRoleId())).thenReturn(Optional.of(role));
        when(userRoleRepository.save(userRole)).thenReturn(userRole);
        when(userRoleMapper.toDTO(userRole)).thenReturn(userRoleDTO);

        UserRoleDTO result = userRoleService.updateUserRole(userRoleId, userRoleDTO);

        assertNotNull(result);
        verify(userRoleRepository, times(1)).findById(userRoleId);
        verify(userRepository, times(1)).findById(userRoleDTO.getUserId());
        verify(roleRepository, times(1)).findById(userRoleDTO.getRoleId());
        verify(userRoleRepository, times(1)).save(userRole);
        verify(userRoleMapper, times(1)).toDTO(userRole);
    }

    @Test
    void testUpdateUserRole_UserRoleNotFound() {
        Long userRoleId = 1L;
        UserRoleDTO userRoleDTO = new UserRoleDTO();

        when(userRoleRepository.findById(userRoleId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> userRoleService.updateUserRole(userRoleId, userRoleDTO));
        assertEquals("Relation User_Role non trouvée", exception.getMessage());
    }

    @Test
    void testDeleteUserRole_Success() throws Exception {
        Long userRoleId = 1L;
        UserRole userRole = new UserRole();

        when(userRoleRepository.findById(userRoleId)).thenReturn(Optional.of(userRole));

        userRoleService.deleteUserRole(userRoleId);

        verify(userRoleRepository, times(1)).delete(userRole);
    }

    @Test
    void testDeleteUserRole_UserRoleNotFound() {
        Long userRoleId = 1L;

        when(userRoleRepository.findById(userRoleId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> userRoleService.deleteUserRole(userRoleId));
        assertEquals("Relation User_Role non trouvée", exception.getMessage());
    }

    @Test
    void testGetAllUserRoles_Success() {
        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(new UserRole());

        when(userRoleRepository.findAll()).thenReturn(userRoles);
        when(userRoleMapper.toDTOs(userRoles)).thenReturn(new ArrayList<>());

        List<UserRoleDTO> result = userRoleService.getAllUserRoles();

        assertNotNull(result);
        verify(userRoleRepository, times(1)).findAll();
        verify(userRoleMapper, times(1)).toDTOs(userRoles);
    }

    @Test
    void testGetUserRoleById_Success() throws Exception {
        Long userRoleId = 1L;
        UserRole userRole = new UserRole();
        UserRoleDTO userRoleDTO = new UserRoleDTO();

        when(userRoleRepository.findById(userRoleId)).thenReturn(Optional.of(userRole));
        when(userRoleMapper.toDTO(userRole)).thenReturn(userRoleDTO);

        UserRoleDTO result = userRoleService.getUserRoleById(userRoleId);

        assertNotNull(result);
        verify(userRoleRepository, times(1)).findById(userRoleId);
        verify(userRoleMapper, times(1)).toDTO(userRole);
    }

    @Test
    void testGetUserRoleById_UserRoleNotFound() {
        Long userRoleId = 1L;

        when(userRoleRepository.findById(userRoleId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> userRoleService.getUserRoleById(userRoleId));
        assertEquals("Relation User_Role non trouvée", exception.getMessage());
    }
}
