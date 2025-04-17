package m1.info.reza.user_feedback.DTO;

public record SubGroupResponse(
        Long id,
        String name,
        String description,
        Long parentGroupId,
        int subGroupCount,
        int userCount
) {}
