package org.codechallenge.social.web;

import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

class FieldDescriptors {

    private FieldDescriptors(){}

    static final FieldDescriptor[] MESSAGE_FIELD_DESCRIPTORS =  new FieldDescriptor[] {
            fieldWithPath("authorId").description("User ID of message author"),
            fieldWithPath("postedAt").description("Timestamp (epoch time) when message was posted"),
            fieldWithPath("content").description("Message content")};
}
