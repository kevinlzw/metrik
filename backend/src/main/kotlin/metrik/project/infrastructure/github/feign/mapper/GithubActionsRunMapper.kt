package metrik.project.infrastructure.github.feign.mapper

import metrik.project.domain.service.githubactions.GithubActionsRun
import metrik.project.domain.service.githubactions.GithubCommit
import metrik.project.infrastructure.github.feign.response.CommitResponse
import metrik.project.infrastructure.github.feign.response.SingleRunResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface GithubActionsRunMapper {
    @Mappings(
        Mapping(target = "branch", source = "headBranch"),
        Mapping(target = "commitTimeStamp", source = "headCommit.timestamp"),
        Mapping(target = "createdTimestamp", source = "createdAt"),
        Mapping(target = "updatedTimestamp", source = "updatedAt"),
    )
    fun mapToGithubActionsRun(run: SingleRunResponse): GithubActionsRun

    @Mappings(
        Mapping(target = "id", source = "sha"),
        Mapping(target = "timestamp", source = "commit.committer.date"),
    )
    fun mapToGithubActionsCommit(commit: CommitResponse): GithubCommit

    companion object {
        val MAPPER: GithubActionsRunMapper = Mappers.getMapper(GithubActionsRunMapper::class.java)
    }
}
