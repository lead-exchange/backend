package lead.exchange.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.UUID;
import java.util.stream.Stream;
import lead.exchange.IntegrationTest;
import lead.exchange.entity.Match;
import lead.exchange.model.MatchStatus;
import lead.exchange.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;


public class MatchControllerTest extends IntegrationTest {

    private UUID testLeadId;
    private UUID testEstateId;
    private UUID testUserLeadId;
    private UUID testUserEstateId;
    @Autowired private MatchRepository matchRepository;

    @BeforeEach
    public void setup() {
        matchRepository.deleteAll();
        testUserLeadId = testData.createTestUser().getId();
        testUserEstateId = testData.createTestUser().getId();
        testLeadId = testData.createTestLead(testUserLeadId).getId();
        testEstateId = testData.createTestEstate(testUserEstateId).getId();
    }

    @ParameterizedTest
    @EnumSource(value = MatchStatus.class, names = {"LIKED", "DISLIKED", "COMMISSION"})
    public void testCreateMatchByLead_success(MatchStatus status) {
        String createMatchJson = String.format(
            """
                    {
                        "leadId": "%s",
                        "estateId": "%s",
                        "leadCommission": 5.5,
                        "updatedBy": "%s",
                        "comment": "Initial match comment",
                        "status": "%s"
                    }
                """, testLeadId, testEstateId, testUserLeadId, status.toString()
        );

        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .body(createMatchJson)
            .when()
            .post("/api/matches")
            .then()
            .statusCode(200)
            .body("leadId", equalTo(testLeadId.toString()))
            .body("estateId", equalTo(testEstateId.toString()))
            .body("leadCommission", equalTo(5.5f))
            .body("comment", equalTo("Initial match comment"))
            .body("leadStatus", equalTo(status.toString()))
            .body("estateStatus", equalTo("UNDEFINED"));
    }

    @ParameterizedTest
    @EnumSource(value = MatchStatus.class, names = {"UNDEFINED", "ACCEPTED", "DECLINED"})
    public void testCreateMatchByLead_failed(MatchStatus status) {
        String createMatchJson = String.format(
            """
                    {
                        "leadId": "%s",
                        "estateId": "%s",
                        "leadCommission": 5.5,
                        "updatedBy": "%s",
                        "comment": "Initial match comment",
                        "status": "%s"
                    }
                """, testLeadId, testEstateId, testUserLeadId, status.toString()
        );

        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .body(createMatchJson)
            .post("/api/matches")
            .then()
            .assertThat(status().isBadRequest());
    }

    @ParameterizedTest
    @EnumSource(value = MatchStatus.class, names = {"LIKED", "DISLIKED", "COMMISSION"})
    public void testCreateMatchByEstate_success(MatchStatus status) {
        String createMatchJson = String.format(
            """
                    {
                        "leadId": "%s",
                        "estateId": "%s",
                        "leadCommission": 5.5,
                        "updatedBy": "%s",
                        "comment": "Initial match comment",
                        "status": "%s"
                    }
                """, testLeadId, testEstateId, testUserEstateId, status.toString()
        );

        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .body(createMatchJson)
            .when()
            .post("/api/matches")
            .then()
            .statusCode(200)
            .body("leadId", equalTo(testLeadId.toString()))
            .body("estateId", equalTo(testEstateId.toString()))
            .body("leadCommission", equalTo(5.5f))
            .body("comment", equalTo("Initial match comment"))
            .body("leadStatus", equalTo("UNDEFINED"))
            .body("estateStatus", equalTo(status.toString()));
    }

    @ParameterizedTest
    @EnumSource(value = MatchStatus.class, names = {"UNDEFINED", "ACCEPTED", "DECLINED"})
    public void testCreateMatchByEstate_failed(MatchStatus status) {
        String createMatchJson = String.format(
            """
                    {
                        "leadId": "%s",
                        "estateId": "%s",
                        "leadCommission": 5.5,
                        "updatedBy": "%s",
                        "comment": "Initial match comment",
                        "status": "%s"
                    }
                """, testLeadId, testEstateId, testUserEstateId, status.toString()
        );

        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .body(createMatchJson)
            .when()
            .post("/api/matches")
            .then()
            .assertThat(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("provideMatchStatuses")
    public void testUpdateMatchByLead_success(
        MatchStatus inputStatusLead,
        MatchStatus inputStatusEstate,
        MatchStatus updateEstateStatus
    ) {
        Match testMatch = testData.createTestMatch(
            testLeadId,
            testEstateId,
            testUserLeadId,
            inputStatusLead,
            inputStatusEstate
        );

        String updateMatchJson = String.format(
            """
                {
                    "id": "%s",
                    "leadCommission": 5.5,
                    "updatedBy": "%s",
                    "comment": "Initial match comment",
                    "status": "%s"
                }
                """, testMatch.getId(), testUserEstateId, updateEstateStatus.toString()
        );

        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .body(updateMatchJson)
            .when()
            .put("/api/matches")
            .then()
            .statusCode(200)
            .body("leadStatus", equalTo(inputStatusLead.toString()))
            .body("estateStatus", equalTo(updateEstateStatus.toString()))
            .body("leadCommission", equalTo(5.5f))
            .body("comment", equalTo("Initial match comment"));
    }

    @ParameterizedTest
    @MethodSource("provideFailedMatchStatuses")
    public void testUpdateMatchByLead_failed(
        MatchStatus inputStatusLead,
        MatchStatus inputStatusEstate,
        MatchStatus updateEstateStatus
    ) {
        Match testMatch = testData.createTestMatch(
            testLeadId,
            testEstateId,
            testUserLeadId,
            inputStatusLead,
            inputStatusEstate
        );

        String updateMatchJson = String.format(
            """
                {
                    "id": "%s",
                    "leadCommission": 5.5,
                    "updatedBy": "%s",
                    "comment": "Initial match comment",
                    "status": "%s"
                }
                """, testMatch.getId(), testUserEstateId, updateEstateStatus.toString()
        );

        RestAssuredMockMvc.given()
            .contentType(ContentType.JSON)
            .body(updateMatchJson)
            .when()
            .put("/api/matches")
            .then()
            .statusCode(400);
    }

    private static Stream<Arguments> provideMatchStatuses() {
        return Stream.of(
            Arguments.of(MatchStatus.LIKED, MatchStatus.UNDEFINED, MatchStatus.LIKED),
            Arguments.of(MatchStatus.LIKED, MatchStatus.UNDEFINED, MatchStatus.DISLIKED),
            Arguments.of(MatchStatus.LIKED, MatchStatus.UNDEFINED, MatchStatus.COMMISSION),
            Arguments.of(MatchStatus.COMMISSION, MatchStatus.UNDEFINED, MatchStatus.COMMISSION),
            Arguments.of(MatchStatus.COMMISSION, MatchStatus.UNDEFINED, MatchStatus.ACCEPTED),
            Arguments.of(MatchStatus.COMMISSION, MatchStatus.UNDEFINED, MatchStatus.DECLINED),
            Arguments.of(MatchStatus.COMMISSION, MatchStatus.COMMISSION, MatchStatus.DECLINED),
            Arguments.of(MatchStatus.COMMISSION, MatchStatus.COMMISSION, MatchStatus.ACCEPTED),
            Arguments.of(MatchStatus.COMMISSION, MatchStatus.COMMISSION, MatchStatus.COMMISSION)
        );
    }

    private static Stream<Arguments> provideFailedMatchStatuses() {
        return Stream.of(
            Arguments.of(MatchStatus.LIKED, MatchStatus.UNDEFINED, MatchStatus.ACCEPTED),
            Arguments.of(MatchStatus.LIKED, MatchStatus.UNDEFINED, MatchStatus.DECLINED),
            Arguments.of(MatchStatus.DISLIKED, MatchStatus.UNDEFINED, MatchStatus.LIKED),
            Arguments.of(MatchStatus.DISLIKED, MatchStatus.UNDEFINED, MatchStatus.DISLIKED),
            Arguments.of(MatchStatus.DISLIKED, MatchStatus.UNDEFINED, MatchStatus.COMMISSION),
            Arguments.of(MatchStatus.DISLIKED, MatchStatus.UNDEFINED, MatchStatus.ACCEPTED),
            Arguments.of(MatchStatus.DISLIKED, MatchStatus.UNDEFINED, MatchStatus.DECLINED),
            Arguments.of(MatchStatus.COMMISSION, MatchStatus.UNDEFINED, MatchStatus.LIKED),
            Arguments.of(MatchStatus.COMMISSION, MatchStatus.UNDEFINED, MatchStatus.DISLIKED),
            Arguments.of(MatchStatus.DECLINED, MatchStatus.COMMISSION, MatchStatus.LIKED),
            Arguments.of(MatchStatus.DECLINED, MatchStatus.COMMISSION, MatchStatus.DISLIKED),
            Arguments.of(MatchStatus.DECLINED, MatchStatus.COMMISSION, MatchStatus.COMMISSION),
            Arguments.of(MatchStatus.DECLINED, MatchStatus.COMMISSION, MatchStatus.ACCEPTED),
            Arguments.of(MatchStatus.DECLINED, MatchStatus.COMMISSION, MatchStatus.DECLINED),
            Arguments.of(MatchStatus.COMMISSION, MatchStatus.COMMISSION, MatchStatus.LIKED),
            Arguments.of(MatchStatus.COMMISSION, MatchStatus.COMMISSION, MatchStatus.DISLIKED)
        );
    }

}
