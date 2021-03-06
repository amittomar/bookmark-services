package com.assignment.controller;

import com.assignment.model.BookmarkCardDto;
import com.assignment.model.BookmarkCardResponse;
import com.assignment.port.BookmarkCardPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/bookmark-management")
public class BookmarkCardController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookmarkCardController.class);

    private final BookmarkCardPort bookmarkCardPort;

    @Autowired
    public BookmarkCardController(BookmarkCardPort bookmarkCardPort) {
        this.bookmarkCardPort = bookmarkCardPort;
    }

    @GetMapping("/cards")
    @Operation(summary = "Get all bookmark cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookmarkCardResponse.class))}),
            @ApiResponse(responseCode = "201", description = "Bookmark created !", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookmarkCardResponse.class))}),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")})
    public ResponseEntity<BookmarkCardResponse> findAllBookmarkCards() {
        LOGGER.info("Get all bookmark cards.");

        BookmarkCardResponse response = new BookmarkCardResponse();
        List<BookmarkCardDto> bookmarkCardDtos = bookmarkCardPort.findAllBookmarkCards();
        response.setBookmarkCards(bookmarkCardDtos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cards/{cardId}")
    @Operation(summary = "Get a bookmark card by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookmarkCardResponse.class))}),
            @ApiResponse(responseCode = "201", description = "Bookmark created !", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookmarkCardResponse.class))}),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")})
    public ResponseEntity<BookmarkCardResponse> findBookmarkCardById(@PathVariable(value = "cardId", required = true) Long cardId) {
        LOGGER.info("Get bookmark card by Id.");

        if (cardId == null || cardId ==0) {
            LOGGER.info("Invalid bookmark card Id.");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        BookmarkCardResponse response = new BookmarkCardResponse();
        List<BookmarkCardDto> bookmarkCardDtoList = new ArrayList<>();
        BookmarkCardDto bookmarkCardDto = bookmarkCardPort.findBookmarkCardById(cardId);

        if(bookmarkCardDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookmarkCardDtoList.add(bookmarkCardDto);

        response.setBookmarkCards(bookmarkCardDtoList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/cards", headers = "Accept=application/json")
    @Operation(summary = "Add new bookmark card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookmarkCardDto.class))}),
            @ApiResponse(responseCode = "201", description = "Bookmark updated !", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookmarkCardDto.class))}),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")})
    public ResponseEntity<BookmarkCardDto> createBookmarkCard(@Valid @RequestBody BookmarkCardDto bookmarkCardDto) {
        LOGGER.info("Create bookmark card.");

        if(bookmarkCardDto == null) {
            LOGGER.info("Invalid input cannot be saved.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(bookmarkCardPort.saveOrUpdateBookmarkCard(bookmarkCardDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/cards/{cardId}", headers = "Accept=application/json")
    @Operation(summary = "Modify bookmark card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookmarkCardDto.class))}),
            @ApiResponse(responseCode = "201", description = "Bookmark updated ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookmarkCardDto.class))}),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")})
    public ResponseEntity<BookmarkCardDto> updateBookmarkCard(@Valid @RequestBody BookmarkCardDto bookmarkCardDto, @PathVariable(value = "cardId", required = true) Long cardId) {
        LOGGER.info("Modify bookmark card.");

        if(bookmarkCardDto == null) {
            LOGGER.info("Invalid input cannot be edited.");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        bookmarkCardDto.setId(cardId);
        return new ResponseEntity<>(bookmarkCardPort.saveOrUpdateBookmarkCard(bookmarkCardDto),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/cards/{cardId}")
    @Operation(summary = "Delete bookmark card by bookmark Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "201", description = "Bookmark updated !", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")})
    public ResponseEntity<Integer> deleteBookmarkById(@PathVariable(value = "cardId", required = true) Long cardId) {
        LOGGER.info("Delete bookmark card by Id.");

        if (cardId == null || cardId ==0) {
            LOGGER.info("Invalid bookmark card Id.");

            return new ResponseEntity<>(-1, HttpStatus.BAD_REQUEST);
        }

        bookmarkCardPort.deleteBookmarkCard(cardId);

        return new ResponseEntity<>(0, HttpStatus.OK);
    }
}
