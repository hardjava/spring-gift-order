package gift.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberRegisterRequestDto;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 성공 시 201(Created)과 accessToken을 반환한다.")
    void shouldRegisterMemberSuccessfully() throws Exception {
        var dto = createRegisterDto();

        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("중복된 이메일로 회원 가입 시 409(Conflict)를 반환한다.")
    void shouldFailToRegisterWithDuplicateEmail() throws Exception {
        var dto = createRegisterDto();

        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("이미 사용 중인 이메일입니다")))
                .andDo(print());
    }

    @Test
    @DisplayName("올바른 정보로 로그인하면 200(OK)과 accessToken을 반환한다.")
    void shouldLoginSuccessfullyWithCorrectCredentials() throws Exception {
        var registerDto = createRegisterDto();
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andDo(print());

        var loginDto = createLoginDto("psh@test.com", "1234");

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 401(Unauthorized)을 반환한다.")
    void shouldFailLoginWithIncorrectPassword() throws Exception {
        var registerDto = createRegisterDto();
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andDo(print());

        var loginDto = createLoginDto("psh@test.com", "1212"); // 올바른 비밀 번호는 1234에 해당

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("비밀번호가 일치하지 않습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 ID로 조회하면 회원 정보를 반환한다.")
    void shouldGetMemberById() throws Exception {
        var dto = createRegisterDto();
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());

        Member saved = memberRepository.findByEmail("psh@test.com").get();

        mockMvc.perform(get("/api/members/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("솨야"))
                .andExpect(jsonPath("$.email").value("psh@test.com"))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회하면 404(Not Found)를 반환한다.")
    void shouldFailToGetNonExistentMember() throws Exception {
        mockMvc.perform(get("/api/members/9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("해당 회원을 찾을 수 없습니다. id=9999"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 ID로 삭제하면 204(NoContent)를 반환한다.")
    void shouldDeleteMemberById() throws Exception {
        var dto = createRegisterDto();
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());

        Member saved = memberRepository.findByEmail("psh@test.com").get();

        mockMvc.perform(delete("/api/members/" + saved.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID로 삭제하면 404(Not Found)를 반환한다.")
    void shouldFailToDeleteNonExistentMember() throws Exception {
        mockMvc.perform(delete("/api/members/9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("해당 회원을 찾을 수 없습니다. id=9999"))
                .andDo(print());
    }

    private MemberRegisterRequestDto createRegisterDto() {
        return new MemberRegisterRequestDto("솨야", "psh@test.com", "1234");
    }

    private MemberLoginRequestDto createLoginDto(String email, String password) {
        return new MemberLoginRequestDto(email, password);
    }
}
