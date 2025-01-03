package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperImplTest {

    private UserMapperImpl userMapper;

    User user;

    UserDto userDto;

    LocalDateTime localDate = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();

        // A user
        user = new User();
        user.setId(1L);
        user.setEmail("rachel@test.fr");
        user.setLastName("Green");
        user.setFirstName("Rachel");
        user.setPassword("password1");
        user.setAdmin(false);
        user.setCreatedAt(localDate);
        user.setUpdatedAt(localDate);

        // A user dto
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("monica@test.fr");
        userDto.setLastName("Geller");
        userDto.setFirstName("Monica");
        userDto.setAdmin(true);
        userDto.setPassword("password2");
        userDto.setCreatedAt(localDate);
        userDto.setUpdatedAt(localDate);
    }

    /************************* toEntity() ****************************/

    @Test
    void testSuccessToEntity() {
        // When
        User convertedUser = userMapper.toEntity(userDto);
        // Then
        assertNotNull(convertedUser);
        assertEquals(1L, convertedUser.getId());
        assertEquals("monica@test.fr", convertedUser.getEmail());
        assertEquals("Monica", convertedUser.getFirstName());
        assertEquals("Geller", convertedUser.getLastName());
        assertEquals("password2", convertedUser.getPassword());
        assertEquals(true, convertedUser.isAdmin());
        assertEquals(localDate, convertedUser.getCreatedAt());
        assertEquals(localDate, convertedUser.getUpdatedAt());
    }

    @Test
    void testSuccessListToEntity() {
        // When
        List<User> convertedUsers = userMapper.toEntity(Arrays.asList(userDto, userDto));
        // Then
        assertNotNull(convertedUsers);
        assertEquals(2, convertedUsers.size());
        assertEquals("Monica", convertedUsers.get(0).getFirstName());
    }

    /************************* toDto() ****************************/

    @Test
    void testSuccessToDto() {
        // When
        UserDto convertedUser = userMapper.toDto(user);
        // Then
        assertNotNull(convertedUser);
        assertEquals(1L, convertedUser.getId());
        assertEquals("Rachel", convertedUser.getFirstName());
        assertEquals("Green", convertedUser.getLastName());
        assertEquals("rachel@test.fr", convertedUser.getEmail());
        assertEquals("password1", convertedUser.getPassword());
        assertEquals(localDate, convertedUser.getCreatedAt());
        assertEquals(localDate, convertedUser.getUpdatedAt());
    }

    @Test
    void testSuccessListToDto() {
        // When
        List<UserDto> convertedUsers = userMapper.toDto(Arrays.asList(user, user));
        // Then
        assertNotNull(convertedUsers);
        assertEquals(2, convertedUsers.size());
        assertEquals("Rachel", convertedUsers.get(0).getFirstName());
    }

}