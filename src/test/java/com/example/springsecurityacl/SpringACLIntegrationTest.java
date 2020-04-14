package com.example.springsecurityacl;

import com.example.springsecurityacl.persistence.dao.NoticeMessageRepository;
import com.example.springsecurityacl.persistence.entity.NoticeMessage;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners(listeners = {ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextBeforeModesTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class
})
public class SpringACLIntegrationTest  extends AbstractJUnit4SpringContextTests {

    private static Integer FIRST_MESSGE_ID = 1;
    private static Integer SECOND_MESSAGE_ID = 2;
    private static Integer THIRD_MESSAGE_ID = 3;
    private static String EDITED_CONTENT = "EDITED";

    @Configuration
    @ComponentScan("com.example.springsecurityacl")
    public static class SpringConfig{

    }

    @Autowired
    NoticeMessageRepository repo;

    @Test
    @WithMockUser(username = "manager")
    public void givenUserManager_whenFindAllMessage_thenReturnFirstMessage() {
        List<NoticeMessage> details = repo.findAll();
       Assert.assertNotNull(details);
       Assert.assertEquals(1,details.size());
       Assert.assertEquals(FIRST_MESSGE_ID,details.get(0).getId());
    }

    @Test
    @WithMockUser(username = "manager")
    public void givenUserManager_whenFindFirstMessage_thenReturnFirstMessage() {
        Optional<NoticeMessage> firstNotacEMessage = repo.findById(FIRST_MESSGE_ID);
        Assert.assertNotNull(firstNotacEMessage);
        Assert.assertEquals(FIRST_MESSGE_ID,firstNotacEMessage.get().getId());

        firstNotacEMessage.get().setContent(EDITED_CONTENT);
        repo.save(firstNotacEMessage.get());

        NoticeMessage editedFirstMessage = repo.getOne(FIRST_MESSGE_ID);
        Assert.assertNotNull(editedFirstMessage);
        Assert.assertEquals(FIRST_MESSGE_ID, editedFirstMessage.getId());
        Assert.assertEquals(EDITED_CONTENT, editedFirstMessage.getContent());
    }

    @Test
    @WithMockUser(username = "hr")
    public void givenUsernameHr_whenFindMessageById2_thenOK(){
        Optional<NoticeMessage> secondMessage = repo.findById(SECOND_MESSAGE_ID);
        Assert.assertNotNull(secondMessage);
        Assert.assertEquals(SECOND_MESSAGE_ID,secondMessage.get().getId());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "hr")
    public void givenUsernameHr_whenUpdateMessageWithId2_thenFail(){
        NoticeMessage secondMessage = new NoticeMessage();
        secondMessage.setId(SECOND_MESSAGE_ID);
        secondMessage.setContent(EDITED_CONTENT);
        repo.save(secondMessage);
    }


    @Test
    @WithMockUser(roles = {"EDITOR"})
    public void givenRoleEditor_whenFindAllMessage_thenReturn3Message() {
        List<NoticeMessage> details = repo.findAll();
        Assert.assertNotNull(details);
        Assert.assertEquals(3,details.size());
    }

    @Test
    @WithMockUser(roles = {"EDITOR"})
    public void givenRoleEditor_whenUpdateThirdMessage_thenOK(){
        NoticeMessage noticeMessage = new NoticeMessage();
        noticeMessage.setId(THIRD_MESSAGE_ID);
        noticeMessage.setContent(EDITED_CONTENT);
        repo.save(noticeMessage);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = {"EDITOR"})
    public void gidenRoleEditor_whenFindFirstMessageByIdAndUpdateContent_thenFail(){
        Optional<NoticeMessage> firstMessage = repo.findById(FIRST_MESSGE_ID);
        Assert.assertNotNull(firstMessage.get());
        Assert.assertEquals(FIRST_MESSGE_ID, firstMessage.get().getId());
        firstMessage.get().setContent(EDITED_CONTENT);
        repo.save(firstMessage.get());
    }
}
